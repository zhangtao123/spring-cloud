package com.anji.allways.business.entity;

public class DictEntity {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_dict.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_dict.dict_key
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  dictKey;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_dict.dict_value
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  dictValue;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_dict.dict_category_Id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer dictCategoryId;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_dict.sort_num
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer sortNum;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_dict.is_valid
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer isValid;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_dict.id
     * @return the value of tb_dict.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_dict.id
     * @param id
     *            the value for tb_dict.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_dict.dict_key
     * @return the value of tb_dict.dict_key
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getDictKey() {
        return dictKey;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_dict.dict_key
     * @param dictKey
     *            the value for tb_dict.dict_key
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setDictKey(String dictKey) {
        this.dictKey = dictKey == null ? null : dictKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_dict.dict_value
     * @return the value of tb_dict.dict_value
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getDictValue() {
        return dictValue;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_dict.dict_value
     * @param dictValue
     *            the value for tb_dict.dict_value
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setDictValue(String dictValue) {
        this.dictValue = dictValue == null ? null : dictValue.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_dict.dict_category_Id
     * @return the value of tb_dict.dict_category_Id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getDictCategoryId() {
        return dictCategoryId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_dict.dict_category_Id
     * @param dictCategoryId
     *            the value for tb_dict.dict_category_Id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setDictCategoryId(Integer dictCategoryId) {
        this.dictCategoryId = dictCategoryId;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_dict.sort_num
     * @return the value of tb_dict.sort_num
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getSortNum() {
        return sortNum;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_dict.sort_num
     * @param sortNum
     *            the value for tb_dict.sort_num
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_dict.is_valid
     * @return the value of tb_dict.is_valid
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getIsValid() {
        return isValid;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_dict.is_valid
     * @param isValid
     *            the value for tb_dict.is_valid
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}