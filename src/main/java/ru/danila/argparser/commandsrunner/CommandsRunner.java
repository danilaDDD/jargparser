package ru.danila.argparser.commandsrunner;

import ru.danila.argparser.exceptions.RunCommandsException;

public interface CommandsRunner {
    void runAllSync() throws RunCommandsException;

    void runAllAsync() throws RunCommandsException;
}
