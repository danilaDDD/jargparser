package ru.danila.argparser.handler;

import java.util.List;

public interface HandleArgs {
  <R> R getPositionValue(int index);

  int getPositionValuesSize();

  <R> R getKeyValue(String paramShortName);
}
