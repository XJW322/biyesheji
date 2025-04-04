package com.rabbiter.association.dao;

import org.springframework.stereotype.Repository;
import com.rabbiter.association.entity.KmeansModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Repository
public interface KmeansModelMapper extends BaseMapper<KmeansModel> {
    @Select("SELECT * FROM kmeans_model ORDER BY cluster_id")
    List<KmeansModel> selectAllCenters();
}
