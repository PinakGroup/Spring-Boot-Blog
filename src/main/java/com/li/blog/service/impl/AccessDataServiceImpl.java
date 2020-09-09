package com.li.blog.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.li.blog.beans.AccessData;
import com.li.blog.beans.Article;
import com.li.blog.mapper.AccessDataMapper;
import com.li.blog.service.AccessDataService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.li.blog.utils.DateUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 访问统计 服务实现类
 * </p>
 *
 * @author li
 * @since 2018-09-17
 */
@Service
public class AccessDataServiceImpl extends ServiceImpl<AccessDataMapper, AccessData> implements AccessDataService {

    List<Integer> selectHotData(Integer days) {
        //查询一周内的访问数据
        Date weekAgo = DateUtil.getPastDate(days);
        ArrayList<String> columns = new ArrayList<>();
        columns.add("access_num");
        EntityWrapper<AccessData> accessDataEntityWrapper = new EntityWrapper<>();
        accessDataEntityWrapper.ge("access_date", weekAgo);
        accessDataEntityWrapper.orderDesc(columns);
        accessDataEntityWrapper.setSqlSelect("article_id");
        List<AccessData> accessData = super.selectList(accessDataEntityWrapper);
        ArrayList<Integer> articleIds = new ArrayList<>();
        for (AccessData ad : accessData) {
            articleIds.add(ad.getArticleId());
        }
        return articleIds;
    }

    void updateData(Integer articleId) {
        EntityWrapper<AccessData> entityWrapper = new EntityWrapper<>();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);
        entityWrapper.eq("article_id", articleId).eq("access_date", dateString);
        AccessData accessData = super.selectOne(entityWrapper);
        if (accessData != null) {
            accessData.setAccessNum(accessData.getAccessNum() + 1);
            super.updateById(accessData);
        } else {
            accessData = new AccessData();
            accessData.setAccessNum(1);
            accessData.setAccessDate(new Date());
            accessData.setArticleId(articleId);
            super.insert(accessData);
        }
    }
}
