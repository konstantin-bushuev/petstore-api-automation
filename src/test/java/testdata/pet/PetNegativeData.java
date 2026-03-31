package testdata.pet;

public class PetNegativeData {
    public static final String VALID_JSON = "{" +
            "\"name\":\"Cat\",\"" +
            "photoUrls\":[\"http://example.com\"]" +
            "}";

    public static final String BAD_JSON = "{name";

    public static final String BAD_STRUCTURE_JSON = "{" +
            "\"name\":\"Cat\"," +
            "\"photoUrls\":\"http://example.com\"" +
            "}";

    public static final Long UNEXISTENT_ID = Long.MAX_VALUE - 100;

    public static final String INVALID_ID = "invalid";

    public static final String VALID_JSON_INVALID_ID = "{" +
            "\"id\":" + INVALID_ID + ",\"" +
            "\"name\":\"Cat\",\"" +
            "photoUrls\":[\"http://example.com\"]" +
            "}";

}
