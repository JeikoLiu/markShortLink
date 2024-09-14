package com.jeiko.shortlink_demo.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.jeiko.shortlink_demo.project.dao.entity.LinkAccessStatsDO;
import com.jeiko.shortlink_demo.project.dao.entity.LinkDeviceStatsDO;
import com.jeiko.shortlink_demo.project.dao.entity.LinkLocaleStatsDO;
import com.jeiko.shortlink_demo.project.dao.entity.LinkNetworkStatsDO;
import com.jeiko.shortlink_demo.project.dao.mapper.*;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkStatsReqDTO;
import com.jeiko.shortlink_demo.project.dto.resp.*;
import com.jeiko.shortlink_demo.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

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
        // 基础访问详情
        List<LinkAccessStatsDO> listShortLinkStats = linkAccessStatsMapper.listShortLinkStats(requestParam);
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
        int oldUserCnt = Integer.parseInt(findUvTypeCnt.get("oldUserCnt").toString());
        int newUserCnt = Integer.parseInt(findUvTypeCnt.get("newUserCnt").toString());
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
                .daily(BeanUtil.copyToList(listShortLinkStats, ShortLinkStatsAccessDailyRespDTO.class))
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
}
