package com.anji.allways.business.entity;

public class CategoryDictEntity {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_category_dict.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_category_dict.category_code
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  categoryCode;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_category_dict.category_name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  categoryName;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_category_dict.description
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  description;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_category_dict.is_valid
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer isValid;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_category_dict.id
     * @return the value of tb_category_dict.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_category_dict.id
     * @param id
     *            the value for tb_category_dict.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_category_dict.category_code
     * @return the value of tb_category_dict.category_code
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getCategoryCode() {
        return categoryCode;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_category_dict.category_code
     * @param categoryCode
     *            the value for tb_category_dict.category_code
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode == null ? null : categoryCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_category_dict.category_name
     * @return the value of tb_category_dict.category_name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_category_dict.category_name
     * @param categoryName
     *            the value for tb_category_dict.category_name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_category_dict.description
     * @return the value of tb_category_dict.description
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_category_dict.description
     * @param description
     *            the value for tb_category_dict.description
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_category_dict.is_valid
     * @return the value of tb_category_dict.is_valid
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getIsValid() {
        return isValid;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_category_dict.is_valid
     * @param isValid
     *            the value for tb_category_dict.is_valid
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}
