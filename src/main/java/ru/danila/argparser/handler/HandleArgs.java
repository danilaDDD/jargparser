package ru.danila.argparser.handler;

public interface HandleArgs {
  <R> R getPositionValue(int index);

  <R> R getKeyValue(String paramShortName);
}
