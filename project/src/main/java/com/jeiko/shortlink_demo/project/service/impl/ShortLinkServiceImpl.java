package com.jeiko.shortlink_demo.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeiko.shortlink_demo.project.common.convention.exception.ClientException;
import com.jeiko.shortlink_demo.project.common.convention.exception.ServiceException;
import com.jeiko.shortlink_demo.project.common.enums.ValidateTypeEnum;
import com.jeiko.shortlink_demo.project.dao.entity.ShortLinkDO;
import com.jeiko.shortlink_demo.project.dao.mapper.ShortLinkMapper;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkCreateReqDTO;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkPageReqDTO;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkUpdateReqDTO;
import com.jeiko.shortlink_demo.project.dto.resp.ShortLinkCreateRespDTO;
import com.jeiko.shortlink_demo.project.dto.resp.ShortLinkGroupCountRespDTO;
import com.jeiko.shortlink_demo.project.dto.resp.ShortLinkPageRespDTO;
import com.jeiko.shortlink_demo.project.service.ShortLinkService;
import com.jeiko.shortlink_demo.project.utils.HashUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 短链接接口实现层
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> createShortLinkCachePenetrationBloomFilter;
    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {
        String shortLinkSuffix = generateSuffix(requestParam);
        String fullShortUrl = StrBuilder.create(requestParam.getDomain())
                .append("/")
                .append(shortLinkSuffix)
                .toString();
        // 使用链式构造代替BeanUtils.toBean()
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .domain(requestParam.getDomain())
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .createdType(requestParam.getCreatedType())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .describe(requestParam.getDescribe())
                .shortUri(shortLinkSuffix)
                .enableStatus(0)
                .fullShortUrl(fullShortUrl)
                .build();
        // 处理布隆过滤器误判导致数据插入数据库异常
        try{
            baseMapper.insert(shortLinkDO);
        } catch (DuplicateKeyException ex) {
            LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, fullShortUrl);
            ShortLinkDO hasShortLinkDO = baseMapper.selectOne(queryWrapper);
            if (hasShortLinkDO != null) {
                log.warn("短链接 {} 重复入库", fullShortUrl);
                throw new ServiceException("短链接生成重复");
            }
        }
        createShortLinkCachePenetrationBloomFilter.add(shortLinkSuffix);
        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl(shortLinkDO.getFullShortUrl())
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .build();
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getEnableStatus, 0)
                .eq(ShortLinkDO::getDelFlag, 0)
                .orderByDesc(ShortLinkDO::getCreateTime);
        IPage<ShortLinkDO> resultPage = baseMapper.selectPage(requestParam, queryWrapper);
        return resultPage.convert(each -> BeanUtil.toBean(each, ShortLinkPageRespDTO.class));
    }

    @Override
    public List<ShortLinkGroupCountRespDTO> countShortLinkListGroup(List<String> requestParam) {
        // 执行sql语句："select gid, count(*) from t_link_n where group in [] and enable_status = 0 group by gid"
        QueryWrapper<ShortLinkDO> queryWrapper = Wrappers.query(new ShortLinkDO()).
                select("gid, count(*) as shortLinkCount").
                in("gid", requestParam).
                eq("enable_status", 0).
                groupBy("gid");
        List<Map<String, Object>> listGroup = baseMapper.selectMaps(queryWrapper);
        return BeanUtil.copyToList(listGroup, ShortLinkGroupCountRespDTO.class);
//        Map<String, ShortLinkGroupCountRespDTO> result = new HashMap<>();
//        listGroup.forEach(each -> result.put((String) each.get("gid"), BeanUtil.toBean(each, ShortLinkGroupCountRespDTO.class)));
//        requestParam.forEach(each -> result.putIfAbsent(each, ShortLinkGroupCountRespDTO.builder()
//                        .gid(each)
//                        .shortLinkCount(0)
//                        .build()
//                ));
//        return result;
    }

    @Override
    public void updateShortLink(ShortLinkUpdateReqDTO requestParam) {
        // 构造查询条件
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getGid, requestParam.getOriginGid())
                .eq(ShortLinkDO::getEnableStatus, 0)
                .eq(ShortLinkDO::getDelFlag, 0);
        ShortLinkDO hasShortLinkDO = baseMapper.selectOne(queryWrapper);
        if (hasShortLinkDO == null) {
            throw new ClientException("需要更新的短链接记录不存在");
        }
        // 构造修改对象
        ShortLinkDO updateShortLinkDO = ShortLinkDO.builder()
                .domain(hasShortLinkDO.getDomain())
                .shortUri(hasShortLinkDO.getShortUri())
                .clickNum(hasShortLinkDO.getClickNum())
                .favicon(hasShortLinkDO.getFavicon())
                .createdType(hasShortLinkDO.getCreatedType())
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getOriginGid())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .describe(requestParam.getDescribe())
                .build();
        if (Objects.equals(hasShortLinkDO.getGid(), requestParam.getGid())) {
            LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                    .eq(ShortLinkDO::getGid, requestParam.getGid())
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .eq(ShortLinkDO::getEnableStatus, 0)
                    .set(Objects.equals(requestParam.getValidDateType(), ValidateTypeEnum.PERMANENT.getType()), ShortLinkDO::getValidDate, null);
            baseMapper.update(updateShortLinkDO, updateWrapper);
        } else {
            LambdaUpdateWrapper<ShortLinkDO> deleteWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                    .eq(ShortLinkDO::getGid, requestParam.getOriginGid())
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .eq(ShortLinkDO::getEnableStatus, 0);
            baseMapper.delete(deleteWrapper);
            baseMapper.insert(updateShortLinkDO);
        }
    }

    /**
     * 生成原始链接对应的短链接
     * @param requestParam 创建短链接请求实体
     * @return 生成的短链接
     */
    private String generateSuffix(ShortLinkCreateReqDTO requestParam) {
        int customGenerateCounter = 0;
        String shortUri;
        while (true) {
            if (customGenerateCounter > 10) {
                throw new ServiceException("短链接频繁生成，请稍后再试");
            }
            String originUrl = requestParam.getOriginUrl();
            // 进一步减少短链接生成冲突
            originUrl += System.currentTimeMillis();
            shortUri = HashUtils.hashToBase62(originUrl);
            if (!createShortLinkCachePenetrationBloomFilter.contains(requestParam.getDomain() + "/" + shortUri)) {
                break;
            }
            customGenerateCounter++;
        }
        return shortUri;
    }
}
