package com.ecommerce.newshop1.utils;

import javax.validation.GroupSequence;

@GroupSequence({ValidationGroups.NotBlankGroup.class, ValidationGroups.PatternGroup.class, ValidationGroups.MaxGroup.class})
public interface ValidationSequence {
}
