package ru.danila.argparser.commandsrunner;

import ru.danila.argparser.exceptions.RunCommandsException;

import java.util.List;

public interface CommandsRunner {
    static CommandsRunner of(List<Runnable> runnableList){
        return new SimpleCommandsRunner(runnableList);
    }

    void runAll() throws RunCommandsException;
}
