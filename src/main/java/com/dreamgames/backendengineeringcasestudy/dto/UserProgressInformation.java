package com.dreamgames.backendengineeringcasestudy.dto;

import com.dreamgames.backendengineeringcasestudy.domain.TestGroup;

public record UserProgressInformation(Integer id,
                                      Double coin,
                                      Integer levelAt,
                                      Double helium,
                                      TestGroup testGroup) {
}
