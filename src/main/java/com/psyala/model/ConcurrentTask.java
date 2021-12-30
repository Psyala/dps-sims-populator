package com.psyala.model;

import com.psyala.model.sim.SimulationResult;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConcurrentTask implements Callable<Optional<SimulationResult>> {
    private final Callable<SimulationResult> c;
    private final AtomicBoolean inProgress = new AtomicBoolean(false);
    private final AtomicBoolean done = new AtomicBoolean(false);
    private Future<Optional<SimulationResult>> future = null;

    public ConcurrentTask(Callable<SimulationResult> c) {
        this.c = c;
    }

    @Override
    public Optional<SimulationResult> call() {
        inProgress.set(true);

        SimulationResult result = null;
        try {
            result = c.call();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        inProgress.set(false);
        done.set(true);

        return Optional.ofNullable(result);
    }

    public boolean isDone() {
        return done.get();
    }

    public boolean isInProgress() {
        return inProgress.get();
    }

    public Future<Optional<SimulationResult>> getFuture() {
        return future;
    }

    public void setFuture(Future<Optional<SimulationResult>> future) {
        this.future = future;
    }
}
