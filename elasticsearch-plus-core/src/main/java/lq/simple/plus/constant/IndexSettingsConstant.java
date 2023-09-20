package lq.simple.plus.constant;


public class IndexSettingsConstant {
    public static final String DEFULT = "{\"settings\":{\"index\":{\"number_of_shards\":\"${shards}\",\"number_of_replicas\":\"${replicas}\"}}}";
    public static final String SPECIAL_CHARACTER_FILTERING = "{\"settings\":{\"index\":{\"number_of_shards\":\"${shards}\",\"analysis\":{\"ik_smart\":{\"char_filter\":[\"html_char_filter\"],\"tokenizer\":\"ik_smart\"},\"ik_max_word\":{\"char_filter\":[\"html_char_filter\"],\"tokenizer\":\"ik_max_word\"}},\"number_of_replicas\":\"${replicas}\"}}}";
    public static final String REMOVE_SINGLE_WORDS = "{\"settings\":{\"index\":{\"number_of_shards\":\"${shards}\",\"analysis\":{\"filter\":{\"longer_than_2\":{\"type\":\"length\",\"min\":2}},\"ik_smart\":{\"filter\":[\"lowercase\",\"longer_than_2\"],\"char_filter\":[\"html_char_filter\"],\"tokenizer\":\"ik_smart\"},\"ik_max_word\":{\"filter\":[\"lowercase\",\"longer_than_2\"],\"char_filter\":[\"html_char_filter\"],\"tokenizer\":\"ik_max_word\"}},\"number_of_replicas\":\"${replicas}\"}}}";
}
