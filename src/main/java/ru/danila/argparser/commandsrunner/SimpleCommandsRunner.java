package ru.danila.argparser.commandsrunner;

import ru.danila.argparser.exceptions.RunCommandsException;

import java.util.List;

public class SimpleCommandsRunner implements CommandsRunner{
    List<Runnable> commands;

    public SimpleCommandsRunner(List<Runnable> commands) {
        this.commands = commands;
    }

    @Override
    public void runAll() throws RunCommandsException {
        for(Runnable command: commands)
            command.run();
    }
}
