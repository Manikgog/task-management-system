package ru.gogolin.task.utils;

public class ValidationConstants {

    public static final String REGEXP_VALIDATE_EMAIL = "^(?=.{6,}$)[\\s]*[a-zA-Z0-9]+([!\"#$%&'()*+,\\-.\\/:;<=>?\\[\\]\\^_{}][a-zA-z0-9]+)*@([\\w]+(-[\\w]+)?)(\\.[\\w]+[-][\\w]+)*(\\.[a-z]{2,})+[\\s]*$";

    public static final String REGEXP_VALIDATE_NAME = "^(?=.{1,}$)[a-zA-Z]+(?:[-' ][a-zA-Z]+)*$";
}
