package ru.danila.argparser.handler;

public interface HandleArgs {
  <R> R getPositionValue(int index);

  int getPositionValuesSize();

  <R> R getKeyValue(String paramShortName);
}
