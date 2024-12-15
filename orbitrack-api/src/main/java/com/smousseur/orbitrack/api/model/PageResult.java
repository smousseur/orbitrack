package com.smousseur.orbitrack.api.model;

import java.util.List;

public record PageResult<T>(List<T> items, int total) {}
