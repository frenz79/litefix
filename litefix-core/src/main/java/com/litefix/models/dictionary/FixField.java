package com.litefix.models.dictionary;

import com.litefix.models.fixmessage.FixMessageDictionary.FieldType;

public record FixField(String name, int tag, FieldType type) {

}
