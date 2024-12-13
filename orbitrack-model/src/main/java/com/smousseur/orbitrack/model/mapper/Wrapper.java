package com.smousseur.orbitrack.model.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wrapper<T> {
  /** The Value. */
  private T value;

  public boolean isEmpty() {
    return this.value == null;
  }
}
