package com.anji.allways.business.entity;

public class OriginalBrandEntityKey {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_original_brand.brandName
     * @mbggenerated Mon Sep 18 20:52:55 CST 2017
     */
    private String brandname;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_original_brand.modelYear
     * @mbggenerated Mon Sep 18 20:52:55 CST 2017
     */
    private String modelyear;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_original_brand.subModelName
     * @mbggenerated Mon Sep 18 20:52:55 CST 2017
     */
    private String submodelname;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_original_brand.versionName
     * @mbggenerated Mon Sep 18 20:52:55 CST 2017
     */
    private String versionname;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_original_brand.brandName
     * @return the value of tb_original_brand.brandName
     * @mbggenerated Mon Sep 18 20:52:55 CST 2017
     */
    public String getBrandname() {
        return brandname;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_original_brand.brandName
     * @param brandname
     *            the value for tb_original_brand.brandName
     * @mbggenerated Mon Sep 18 20:52:55 CST 2017
     */
    public void setBrandname(String brandname) {
        this.brandname = brandname == null ? null : brandname.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_original_brand.modelYear
     * @return the value of tb_original_brand.modelYear
     * @mbggenerated Mon Sep 18 20:52:55 CST 2017
     */
    public String getModelyear() {
        return modelyear;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_original_brand.modelYear
     * @param modelyear
     *            the value for tb_original_brand.modelYear
     * @mbggenerated Mon Sep 18 20:52:55 CST 2017
     */
    public void setModelyear(String modelyear) {
        this.modelyear = modelyear == null ? null : modelyear.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_original_brand.subModelName
     * @return the value of tb_original_brand.subModelName
     * @mbggenerated Mon Sep 18 20:52:55 CST 2017
     */
    public String getSubmodelname() {
        return submodelname;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_original_brand.subModelName
     * @param submodelname
     *            the value for tb_original_brand.subModelName
     * @mbggenerated Mon Sep 18 20:52:55 CST 2017
     */
    public void setSubmodelname(String submodelname) {
        this.submodelname = submodelname == null ? null : submodelname.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_original_brand.versionName
     * @return the value of tb_original_brand.versionName
     * @mbggenerated Mon Sep 18 20:52:55 CST 2017
     */
    public String getVersionname() {
        return versionname;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_original_brand.versionName
     * @param versionname
     *            the value for tb_original_brand.versionName
     * @mbggenerated Mon Sep 18 20:52:55 CST 2017
     */
    public void setVersionname(String versionname) {
        this.versionname = versionname == null ? null : versionname.trim();
    }
}