package cn.watsontech.webhelper.openapi.form.wx;

/**
 * Created by watson on 2020/4/14.
 */
public class WxMessageData {
    private String name;
    private String value;
    private String color;

    public WxMessageData() {}
    public WxMessageData(String name, String value) {
        this(name, value, true);
    }
    public WxMessageData(String name, String value, String color) {
        this(name, value, color,true);
    }
    public WxMessageData(String name, String value, boolean isMiniProgram) {
        this(name, value, null, isMiniProgram);
    }
    public WxMessageData(String name, String value, String color, boolean isMiniProgram) {
        this.name = name;
        this.value = isMiniProgram?this.valueCorrect(name, value):value;
        this.color = color;
    }

    /**
     * 参数类别	参数说明	参数值限制	说明
     * thing.DATA	事物	20个以内字符	可汉字、数字、字母或符号组合
     * number.DATA	数字	32位以内数字	只能数字，可带小数
     * letter.DATA	字母	32位以内字母	只能字母
     * symbol.DATA	符号	5位以内符号	只能符号
     * character_string.DATA	字符串	32位以内数字、字母或符号	可数字、字母或符号组合
     * time.DATA	时间	24小时制时间格式（支持+年月日）	例如：15:01，或：2019年10月1日 15:01
     * date.DATA	日期	年月日格式（支持+24小时制时间）	例如：2019年10月1日，或：2019年10月1日 15:01
     * amount.DATA	金额	1个币种符号+10位以内纯数字，可带小数，结尾可带“元”	可带小数
     * phone_number.DATA	电话	17位以内，数字、符号	电话号码，例：+86-0766-66888866
     * car_number.DATA	车牌	8位以内，第一位与最后一位可为汉字，其余为字母或数字	车牌号码：粤A8Z888挂
     * name.DATA	姓名	10个以内纯汉字或20个以内纯字母或符号	中文名10个汉字内；纯英文名20个字母内；中文和字母混合按中文名算，10个字内
     * phrase.DATA	汉字	5个以内汉字	5个以内纯汉字，例如：配送中
     * @param type 参数类型
     * @param value 参数值
     * @return 处理过的合法参数值
     */
    private String valueCorrect(String type, String value) {
        if(type.startsWith("thing")) {
            return substring(value, 20);
        }else if(type.startsWith("letter")) {
            return substring(value, 32);
        }else if(type.startsWith("phone_number")) {
            return substring(value, 17);
        }else if(type.startsWith("phrase")) {
            return substring(value, 5);
        }else if(type.startsWith("name")) {
            return substring(value, 20);
        }

        return value;
    }

    private String substring(String value, int length) {
        if (value!=null) {
            if (value.length()>length) return value.substring(0, length);
        }
        return value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
