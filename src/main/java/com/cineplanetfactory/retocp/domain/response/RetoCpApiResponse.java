package com.cineplanetfactory.retocp.domain.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetoCpApiResponse<T> {
    private T data;
}
