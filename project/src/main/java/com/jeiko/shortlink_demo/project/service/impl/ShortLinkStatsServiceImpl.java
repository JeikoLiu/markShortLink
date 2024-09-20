package com.jeiko.shortlink_demo.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jeiko.shortlink_demo.project.dao.entity.*;
import com.jeiko.shortlink_demo.project.dao.mapper.*;
import com.jeiko.shortlink_demo.project.dto.req.*;
import com.jeiko.shortlink_demo.project.dto.resp.*;
import com.jeiko.shortlink_demo.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShortLinkStatsServiceImpl implements ShortLinkStatsService {

    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkLocaleStatsMapper linkLocaleStatsMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkOsStatsMapper linkOsStatsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkNetworkStatsMapper linkNetworkStatsMapper;

    @Override
    public ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        List<LinkAccessStatsDO> listShortLinkStats = linkAccessStatsMapper.listShortLinkStats(requestParam);
        if (CollUtil.isEmpty(listShortLinkStats)) {
            return null;
        }
        // 基础访问数据（PV/UV/UIP）
        LinkAccessStatsDO pvUvUidStatsByShortLink = linkAccessLogsMapper.findPvUvUidStats(requestParam);

        // 基础访问详情（每天的访问量）
        List<ShortLinkStatsAccessDailyRespDTO> daily = new ArrayList<>();
        List<String> rangeDates = DateUtil.rangeToList(DateUtil.parse(requestParam.getStartDate()), DateUtil.parse(requestParam.getEndDate()), DateField.DAY_OF_MONTH).stream()
                .map(DateUtil::formatDate)
                .collect(Collectors.toList());
        rangeDates.forEach(each -> listShortLinkStats.stream()
                .filter(item -> Objects.equals(each, DateUtil.formatDate(item.getDate())))
                .findFirst()
                .ifPresentOrElse(item -> {
                    ShortLinkStatsAccessDailyRespDTO result = ShortLinkStatsAccessDailyRespDTO.builder()
                            .date(each)
                            .pv(item.getPv())
                            .uv(item.getUv())
                            .uip(item.getUip())
                            .build();
                    daily.add(result);
                }, () -> {
                    ShortLinkStatsAccessDailyRespDTO result = ShortLinkStatsAccessDailyRespDTO.builder()
                            .date(each)
                            .pv(0)
                            .uv(0)
                            .uip(0)
                            .build();
                    daily.add(result);
                })
        );
        // 地区访问详情（仅国内）
        List<LinkLocaleStatsDO> listLinkLocateStats = linkLocaleStatsMapper.listLinkLocateStats(requestParam);
        List<ShortLinkStatsLocaleCNRespDTO> localeCnStats = new ArrayList<>();
        int localeCnSum = listLinkLocateStats.stream()
                .mapToInt(LinkLocaleStatsDO::getCnt)
                .sum();
        listLinkLocateStats.forEach(each -> {
            double radio = (double) each.getCnt()/localeCnSum;
            double actualRadio = Math.round(radio * 100.0) / 100.0;
            ShortLinkStatsLocaleCNRespDTO result = ShortLinkStatsLocaleCNRespDTO.builder()
                    .cnt(each.getCnt())
                    .locale(each.getProvince())
                    .ratio(actualRadio)
                    .build();
            localeCnStats.add(result);
        });

        // 小时访问详情
        List<Integer> hourStats = new ArrayList<>();
        List<LinkAccessStatsDO> listHourShortLinkStats = linkAccessStatsMapper.listHourShortLinkStats(requestParam);
        for (int i = 0; i < 24; i++) {
            AtomicInteger hour = new AtomicInteger(i);
            int hourCnt = listHourShortLinkStats.stream()
                    .filter(each -> Objects.equals(hour.get(), each.getHour()))
                    .findFirst()
                    .map(LinkAccessStatsDO::getPv)
                    .orElse(0);
            hourStats.add(hourCnt);
        }
        // 一周访问详情
        List<Integer> weekdayStats = new ArrayList<>();
        List<LinkAccessStatsDO> listWeekdayShortLinkStats = linkAccessStatsMapper.listWeekdayShortLinkStats(requestParam);
        for (int i = 0; i < 8; i++) {
            AtomicInteger weekday = new AtomicInteger(i);
            int weekdayCnt = listWeekdayShortLinkStats.stream()
                    .filter(each -> Objects.equals(weekday.get(), each.getWeekday()))
                    .findFirst()
                    .map(LinkAccessStatsDO::getPv)
                    .orElse(0);
            weekdayStats.add(weekdayCnt);
        }

        // 浏览器访问详情
        List<ShortLinkStatsBrowserRespDTO> browserStats = new ArrayList<>();
        List<HashMap<String, Object>> listBrowserStats = linkBrowserStatsMapper.listBrowserStats(requestParam);
        int browserSum = listBrowserStats.stream()
                .mapToInt(each -> Integer.parseInt(each.get("count").toString()))
                .sum();
        listBrowserStats.forEach(each -> {
            double radio = (double) Integer.parseInt(each.get("count").toString()) / browserSum;
            double actualRadio = Math.round(radio * 100.0) / 100.0;
            ShortLinkStatsBrowserRespDTO result = ShortLinkStatsBrowserRespDTO.builder()
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .browser(each.get("browser").toString())
                    .ratio(actualRadio)
                    .build();
            browserStats.add(result);
        });
        // 操作系统访问详情
        List<ShortLinkStatsOsRespDTO> osStats = new ArrayList<>();
        List<HashMap<String, Object>> listOsStats = linkOsStatsMapper.listOsStats(requestParam);
        int osSum = listOsStats.stream()
                .mapToInt(each -> Integer.parseInt(each.get("count").toString()))
                .sum();
        listOsStats.forEach(each -> {
            double radio = (double) Integer.parseInt(each.get("count").toString()) / osSum;
            double actualRadio = Math.round(radio * 100.0) / 100.0;
            ShortLinkStatsOsRespDTO result = ShortLinkStatsOsRespDTO.builder()
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .os(each.get("os").toString())
                    .ratio(actualRadio)
                    .build();
            osStats.add(result);
        });
        // 访问设备类型
        List<ShortLinkStatsDeviceRespDTO> deviceStats = new ArrayList<>();
        List<LinkDeviceStatsDO> listDeviceStats = linkDeviceStatsMapper.listDeviceStats(requestParam);
        int deviceSum = listDeviceStats.stream()
                .mapToInt(LinkDeviceStatsDO::getCnt)
                .sum();
        listDeviceStats.forEach(each -> {
            double radio = (double) each.getCnt() / deviceSum;
            double actualRadio = Math.round(radio * 100.0) / 100.0;
            ShortLinkStatsDeviceRespDTO result = ShortLinkStatsDeviceRespDTO.builder()
                    .cnt(each.getCnt())
                    .device(each.getDevice())
                    .ratio(actualRadio)
                    .build();
            deviceStats.add(result);
        });
        // 访问网络类型
        List<ShortLinkStatsNetworkRespDTO> networkStats = new ArrayList<>();
        List<LinkNetworkStatsDO> listNetworkStats = linkNetworkStatsMapper.listNetworkStats(requestParam);
        int networkSum = listNetworkStats.stream()
                .mapToInt(LinkNetworkStatsDO::getCnt)
                .sum();
        listNetworkStats.forEach(each -> {
            double radio = (double) each.getCnt() / networkSum;
            double actualRadio = Math.round(radio * 100.0) / 100.0;
            ShortLinkStatsNetworkRespDTO result = ShortLinkStatsNetworkRespDTO.builder()
                    .cnt(each.getCnt())
                    .network(each.getNetwork())
                    .ratio(actualRadio)
                    .build();
            networkStats.add(result);
        });

        // 高频访问IP详情
        List<ShortLinkStatsTopIpRespDTO> topIpStats = new ArrayList<>();
        List<HashMap<String, Object>> listTopIpStats = linkAccessLogsMapper.listTopIpStats(requestParam);
        listTopIpStats.forEach(each -> {
            ShortLinkStatsTopIpRespDTO result = ShortLinkStatsTopIpRespDTO.builder()
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .ip(each.get("ip").toString())
                    .build();
            topIpStats.add(result);
        });
        // 访客访问类型详情
        List<ShortLinkStatsUvRespDTO> uvTypeStats = new ArrayList<>();
        HashMap<String, Object> findUvTypeCnt = linkAccessLogsMapper.findUvTypeCnt(requestParam);
        int oldUserCnt = Integer.parseInt(
                Optional.ofNullable(findUvTypeCnt)
                        .map(each -> each.get("oldUserCnt"))
                        .map(Object::toString)
                        .orElse("0")
        );
        int newUserCnt = Integer.parseInt(
                Optional.ofNullable(findUvTypeCnt)
                        .map(each -> each.get("newUserCnt"))
                        .map(Object::toString)
                        .orElse("0")
        );
        int uvSum = oldUserCnt + newUserCnt;
        double oldRatio = (double) oldUserCnt / uvSum;
        double actualOldRatio = Math.round(oldRatio * 100.0) / 100.0;
        double newRatio = (double) newUserCnt / uvSum;
        double actualNewRatio = Math.round(newRatio * 100.0) / 100.0;
        ShortLinkStatsUvRespDTO newUvRespDTO = ShortLinkStatsUvRespDTO.builder()
                .uvType("newUser")
                .cnt(newUserCnt)
                .ratio(actualNewRatio)
                .build();
        uvTypeStats.add(newUvRespDTO);
        ShortLinkStatsUvRespDTO oldUvRespDTO = ShortLinkStatsUvRespDTO.builder()
                .uvType("oldUser")
                .cnt(oldUserCnt)
                .ratio(actualOldRatio)
                .build();
        uvTypeStats.add(oldUvRespDTO);

        return ShortLinkStatsRespDTO.builder()
                .pv(pvUvUidStatsByShortLink.getPv())
                .uv(pvUvUidStatsByShortLink.getUv())
                .uip(pvUvUidStatsByShortLink.getUip())
                .daily(daily)
                .localeCnStats(localeCnStats)
                .hourStats(hourStats)
                .topIpStats(topIpStats)
                .weekdayStats(weekdayStats)
                .browserStats(browserStats)
                .osStats(osStats)
                .uvTypeStats(uvTypeStats)
                .deviceStats(deviceStats)
                .networkStats(networkStats)
                .build();
    }

    @Override
    public ShortLinkStatsRespDTO groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam) {
        List<LinkAccessStatsDO> listShortLinkStats = linkAccessStatsMapper.listStatsByGroup(requestParam);
        if (CollUtil.isEmpty(listShortLinkStats)) {
            return null;
        }
        // 基础访问数据（PV/UV/UIP）
        LinkAccessStatsDO pvUvUidStatsByShortLink = linkAccessLogsMapper.findPvUvUidStatsByGroup(requestParam);

        // 基础访问详情（每天的访问量）
        List<ShortLinkStatsAccessDailyRespDTO> daily = new ArrayList<>();
        List<String> rangeDates = DateUtil.rangeToList(DateUtil.parse(requestParam.getStartDate()), DateUtil.parse(requestParam.getEndDate()), DateField.DAY_OF_MONTH).stream()
                .map(DateUtil::formatDate)
                .collect(Collectors.toList());
        rangeDates.forEach(each -> listShortLinkStats.stream()
                .filter(item -> Objects.equals(each, DateUtil.formatDate(item.getDate())))
                .findFirst()
                .ifPresentOrElse(item -> {
                    ShortLinkStatsAccessDailyRespDTO result = ShortLinkStatsAccessDailyRespDTO.builder()
                            .date(each)
                            .pv(item.getPv())
                            .uv(item.getUv())
                            .uip(item.getUip())
                            .build();
                    daily.add(result);
                }, () -> {
                    ShortLinkStatsAccessDailyRespDTO result = ShortLinkStatsAccessDailyRespDTO.builder()
                            .date(each)
                            .pv(0)
                            .uv(0)
                            .uip(0)
                            .build();
                })
        );
        // 地区访问详情（仅国内）
        List<LinkLocaleStatsDO> listLinkLocateStats = linkLocaleStatsMapper.listLocaleByGroup(requestParam);
        List<ShortLinkStatsLocaleCNRespDTO> localeCnStats = new ArrayList<>();
        int localeCnSum = listLinkLocateStats.stream()
                .mapToInt(LinkLocaleStatsDO::getCnt)
                .sum();
        listLinkLocateStats.forEach(each -> {
            double radio = (double) each.getCnt()/localeCnSum;
            double actualRadio = Math.round(radio * 100.0) / 100.0;
            ShortLinkStatsLocaleCNRespDTO result = ShortLinkStatsLocaleCNRespDTO.builder()
                    .cnt(each.getCnt())
                    .locale(each.getProvince())
                    .ratio(actualRadio)
                    .build();
            localeCnStats.add(result);
        });

        // 小时访问详情
        List<Integer> hourStats = new ArrayList<>();
        List<LinkAccessStatsDO> listHourShortLinkStats = linkAccessStatsMapper.listHourStatsByGroup(requestParam);
        for (int i = 0; i < 24; i++) {
            AtomicInteger hour = new AtomicInteger(i);
            int hourCnt = listHourShortLinkStats.stream()
                    .filter(each -> Objects.equals(hour.get(), each.getHour()))
                    .findFirst()
                    .map(LinkAccessStatsDO::getPv)
                    .orElse(0);
            hourStats.add(hourCnt);
        }
        // 一周访问详情
        List<Integer> weekdayStats = new ArrayList<>();
        List<LinkAccessStatsDO> listWeekdayShortLinkStats = linkAccessStatsMapper.listWeekdayStatsByGroup(requestParam);
        for (int i = 0; i < 8; i++) {
            AtomicInteger weekday = new AtomicInteger(i);
            int weekdayCnt = listWeekdayShortLinkStats.stream()
                    .filter(each -> Objects.equals(weekday.get(), each.getWeekday()))
                    .findFirst()
                    .map(LinkAccessStatsDO::getPv)
                    .orElse(0);
            weekdayStats.add(weekdayCnt);
        }

        // 浏览器访问详情
        List<ShortLinkStatsBrowserRespDTO> browserStats = new ArrayList<>();
        List<HashMap<String, Object>> listBrowserStats = linkBrowserStatsMapper.listBrowserStatsByGroup(requestParam);
        int browserSum = listBrowserStats.stream()
                .mapToInt(each -> Integer.parseInt(each.get("count").toString()))
                .sum();
        listBrowserStats.forEach(each -> {
            double radio = (double) Integer.parseInt(each.get("count").toString()) / browserSum;
            double actualRadio = Math.round(radio * 100.0) / 100.0;
            ShortLinkStatsBrowserRespDTO result = ShortLinkStatsBrowserRespDTO.builder()
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .browser(each.get("browser").toString())
                    .ratio(actualRadio)
                    .build();
            browserStats.add(result);
        });
        // 操作系统访问详情
        List<ShortLinkStatsOsRespDTO> osStats = new ArrayList<>();
        List<HashMap<String, Object>> listOsStats = linkOsStatsMapper.listOsStatsByGroup(requestParam);
        int osSum = listOsStats.stream()
                .mapToInt(each -> Integer.parseInt(each.get("count").toString()))
                .sum();
        listOsStats.forEach(each -> {
            double radio = (double) Integer.parseInt(each.get("count").toString()) / osSum;
            double actualRadio = Math.round(radio * 100.0) / 100.0;
            ShortLinkStatsOsRespDTO result = ShortLinkStatsOsRespDTO.builder()
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .os(each.get("os").toString())
                    .ratio(actualRadio)
                    .build();
            osStats.add(result);
        });
        // 访问设备类型
        List<ShortLinkStatsDeviceRespDTO> deviceStats = new ArrayList<>();
        List<LinkDeviceStatsDO> listDeviceStats = linkDeviceStatsMapper.listDeviceStatsByGroup(requestParam);
        int deviceSum = listDeviceStats.stream()
                .mapToInt(LinkDeviceStatsDO::getCnt)
                .sum();
        listDeviceStats.forEach(each -> {
            double radio = (double) each.getCnt() / deviceSum;
            double actualRadio = Math.round(radio * 100.0) / 100.0;
            ShortLinkStatsDeviceRespDTO result = ShortLinkStatsDeviceRespDTO.builder()
                    .cnt(each.getCnt())
                    .device(each.getDevice())
                    .ratio(actualRadio)
                    .build();
            deviceStats.add(result);
        });
        // 访问网络类型
        List<ShortLinkStatsNetworkRespDTO> networkStats = new ArrayList<>();
        List<LinkNetworkStatsDO> listNetworkStats = linkNetworkStatsMapper.listNetworkStatsByGroup(requestParam);
        int networkSum = listNetworkStats.stream()
                .mapToInt(LinkNetworkStatsDO::getCnt)
                .sum();
        listNetworkStats.forEach(each -> {
            double radio = (double) each.getCnt() / networkSum;
            double actualRadio = Math.round(radio * 100.0) / 100.0;
            ShortLinkStatsNetworkRespDTO result = ShortLinkStatsNetworkRespDTO.builder()
                    .cnt(each.getCnt())
                    .network(each.getNetwork())
                    .ratio(actualRadio)
                    .build();
            networkStats.add(result);
        });

        // 高频访问IP详情
        List<ShortLinkStatsTopIpRespDTO> topIpStats = new ArrayList<>();
        List<HashMap<String, Object>> listTopIpStats = linkAccessLogsMapper.listTopIpByGroup(requestParam);
        listTopIpStats.forEach(each -> {
            ShortLinkStatsTopIpRespDTO result = ShortLinkStatsTopIpRespDTO.builder()
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .ip(each.get("ip").toString())
                    .build();
            topIpStats.add(result);
        });

        return ShortLinkStatsRespDTO.builder()
                .pv(pvUvUidStatsByShortLink.getPv())
                .uv(pvUvUidStatsByShortLink.getUv())
                .uip(pvUvUidStatsByShortLink.getUip())
                .daily(daily)
                .localeCnStats(localeCnStats)
                .hourStats(hourStats)
                .topIpStats(topIpStats)
                .weekdayStats(weekdayStats)
                .browserStats(browserStats)
                .osStats(osStats)
                .deviceStats(deviceStats)
                .networkStats(networkStats)
                .build();
    }

    @Override
    public IPage<ShortLinkStatsAccessRecordRespDTO> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam) {
        LambdaQueryWrapper<LinkAccessLogsDO> queryWrapper = Wrappers.lambdaQuery(LinkAccessLogsDO.class)
                .eq(LinkAccessLogsDO::getGid, requestParam.getGid())
                .eq(LinkAccessLogsDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(LinkAccessLogsDO::getDelFlag, 0)
                .between(LinkAccessLogsDO::getCreateTime, requestParam.getStartDate(), requestParam.getEndDate())
                .orderByDesc(LinkAccessLogsDO::getCreateTime);
        IPage<LinkAccessLogsDO> linkAccessLogsDOIPage = linkAccessLogsMapper.selectPage(requestParam, queryWrapper);
        IPage<ShortLinkStatsAccessRecordRespDTO> actualResult = linkAccessLogsDOIPage.convert(each -> BeanUtil.toBean(each, ShortLinkStatsAccessRecordRespDTO.class));
        List<String> userAccessList = actualResult.getRecords().stream()
                .map(ShortLinkStatsAccessRecordRespDTO::getUser)
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(userAccessList)) {
            return actualResult;
        }
        // 根据用户查询用户对应的访客类型
        ShortLinkAccessRecordMapperDTO queryParam = ShortLinkAccessRecordMapperDTO.builder()
                .gid(requestParam.getGid())
                .fullShortUrl(requestParam.getFullShortUrl())
                .startDate(requestParam.getStartDate())
                .endDate(requestParam.getEndDate())
                .userAccessList(userAccessList)
                .build();
        List<Map<String, Object>> uvTypeList = linkAccessLogsMapper.selectUvTypeByUser(queryParam);
        actualResult.getRecords().forEach(each -> {
            String uvType = uvTypeList.stream()
                    .filter(item -> Objects.equals(item.get("user"), each.getUser()))
                    .findFirst()
                    .map(item -> item.get("uvType"))
                    .map(Object::toString)
                    .orElse("旧访客");
            each.setUvType(uvType);
        });
        return actualResult;
    }

    @Override
    public IPage<ShortLinkStatsAccessRecordRespDTO> groupShortLinkStatsAccessRecord(ShortLinkGroupStatsAccessRecordReqDTO requestParam) {
        LambdaQueryWrapper<LinkAccessLogsDO> queryWrapper = Wrappers.lambdaQuery(LinkAccessLogsDO.class)
                .eq(LinkAccessLogsDO::getGid, requestParam.getGid())
                .eq(LinkAccessLogsDO::getDelFlag, 0)
                .between(LinkAccessLogsDO::getCreateTime, requestParam.getStartDate(), requestParam.getEndDate())
                .orderByDesc(LinkAccessLogsDO::getCreateTime);
        IPage<LinkAccessLogsDO> linkAccessLogsDOIPage = linkAccessLogsMapper.selectPage(requestParam, queryWrapper);
        IPage<ShortLinkStatsAccessRecordRespDTO> actualResult = linkAccessLogsDOIPage.convert(each -> BeanUtil.toBean(each, ShortLinkStatsAccessRecordRespDTO.class));
        List<String> userAccessList = actualResult.getRecords().stream()
                .map(ShortLinkStatsAccessRecordRespDTO::getUser)
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(userAccessList)) {
            return actualResult;
        }
        // 根据用户查询用户对应的访客类型
        ShortLinkGroupAccessRecordMapperDTO queryParam = ShortLinkGroupAccessRecordMapperDTO.builder()
                .gid(requestParam.getGid())
                .startDate(requestParam.getStartDate())
                .endDate(requestParam.getEndDate())
                .userAccessList(userAccessList)
                .build();
        List<Map<String, Object>> uvTypeList = linkAccessLogsMapper.selectGroupUvTypeByUsers(queryParam);
        actualResult.getRecords().forEach(each -> {
            String uvType = uvTypeList.stream()
                    .filter(item -> Objects.equals(item.get("user"), each.getUser()))
                    .findFirst()
                    .map(item -> item.get("uvType"))
                    .map(Object::toString)
                    .orElse("旧访客");
            each.setUvType(uvType);
        });
        return actualResult;
    }
}
