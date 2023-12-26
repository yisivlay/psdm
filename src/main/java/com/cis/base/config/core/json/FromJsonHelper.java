package com.cis.base.config.core.json;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

/**
 * @author YSivlay
 */
@Getter
@Setter
public class FromJsonHelper {

    private final Gson gsonConverter;
    private final JsonParserHelper helperDelegator;

    public FromJsonHelper() {
        this.gsonConverter = new Gson();
        this.helperDelegator = new JsonParserHelper();
    }
}
