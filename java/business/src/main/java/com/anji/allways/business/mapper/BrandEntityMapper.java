package com.anji.allways.business.mapper;

import java.util.List;
import java.util.Map;

import com.anji.allways.business.entity.BrandEntity;
import com.anji.allways.business.vo.BrandVO;

public interface BrandEntityMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_brand
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_brand
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insert(BrandEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_brand
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insertSelective(BrandEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_brand
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    BrandEntity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_brand
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKeySelective(BrandEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_brand
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKey(BrandEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_brand
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    List<BrandVO> selectAllLetter(Integer userId);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_brand
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    List<BrandVO> selectAllBrand(String initial);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_brand
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    List<BrandVO> selectAllSeries(String brand);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_brand
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    List<BrandVO> selectAllModel(Map<String, Object> map);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int checkYearExist(Map<String, Object> mapVue);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    List<String> selectByBrandToYear(Map<String, Object> map);

    int updateByBrandToLogo(Map<String, Object> mapVue);

    /**
     * 根据客户ID获取品牌信息
     * @param map
     * @return
     */
    List<BrandEntity> selectBrandByCustomerId(Map<String, Object> map);

}
