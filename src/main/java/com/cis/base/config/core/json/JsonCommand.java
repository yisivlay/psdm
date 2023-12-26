package com.cis.base.config.core.json;

import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Immutable representation of a json.
 * Wraps the provided JSON with convenience functions for extracting parameter
 * values and checking for changes against an existing value.
 * @author YSivlay
 */
@Getter
@Setter
@Builder
public class JsonCommand {

    private String jsonCommand;
    private JsonElement parsedCommand;
    private FromJsonHelper fromApiJsonHelper;
    private Long commandId;
    private Long resourceId;
    private Long subResourceId;
    private String url;

}
