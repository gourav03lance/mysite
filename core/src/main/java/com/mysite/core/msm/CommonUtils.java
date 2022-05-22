package com.mysite.core.msm;

import com.day.cq.wcm.api.Page;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

public class CommonUtils {


    public static String getCountryCode(final Page currentPage) {
        Page languagePage = getLanguagePage(currentPage);
        final String country = "us";
        if (Objects.nonNull(languagePage) && Objects.nonNull(languagePage.getParent())
                && Objects.nonNull(languagePage.getParent().getName())) {
            return languagePage.getParent().getName();
        }
        return country;
    }

    public static Page getLanguagePage(final Page currentPage) {
        if (Objects.nonNull(currentPage)) {
            return currentPage.getAbsoluteParent(3);
        }
        return null;
    }

    public static String getLanguageCode(final Page currentPage) {
        if (Objects.nonNull(currentPage)) {
            return currentPage.getLanguage(Boolean.FALSE).getLanguage();
        }
        return StringUtils.EMPTY;
    }
}
