package ai.openfabric.api.service;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.WorkerStats;
import ai.openfabric.api.repository.WorkerRepository;
import ai.openfabric.api.repository.WorkerStatsRepository;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.InvocationBuilder.AsyncResultCallback;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@Component
@EnableScheduling
public class DockerService {

    private final DockerClient client;
    private final WorkerRepository workerRepository;
    private final WorkerStatsRepository workerStatsRepository;

    public DockerService(WorkerStatsRepository workerStatsRepository, WorkerRepository workerRepository, DockerClient client) {
        this.workerRepository = workerRepository;
        this.workerStatsRepository = workerStatsRepository;
        this.client = client;
    }

    public Container getContainer(String container_id) {
        List<Container> containers = client.listContainersCmd().withShowAll(true).withIdFilter(Collections.singletonList(container_id)).exec();
        if (containers.size() == 0) {
            return null;
        }
        return containers.get(0);
    }

    public List<Container> getAllContainers() {
        return client.listContainersCmd().withShowAll(true).exec();
    }

    public void startWorker(String container_id) throws NotModifiedException {
        // perform a null check before executing startWorker command
        if (container_id == null) return;

        client.startContainerCmd(container_id).exec();
    }

    public void stopWorker(String container_id) throws NotModifiedException {
        // perform a null check before executing stopWorker command
        if (container_id == null) return;

        client.stopContainerCmd(container_id).exec();
    }

    @Scheduled(fixedDelay = 2000)
    public void workerStats() {
        System.out.println("worker stats");
        
        List<Container> containers = client.listContainersCmd().exec();
        for (Container container : containers) {
            Worker worker = workerRepository.findByDockerId(container.getId());
            AsyncResultCallback<Statistics> callback = new AsyncResultCallback<>();
            client.statsCmd(container.getId()).exec(callback);
            try {
                Statistics stats = callback.awaitResult();
                callback.close();
                WorkerStats workerStats = WorkerStats.builder().worker(worker).build();
                workerStats.setFromStatistics(stats);
                workerStatsRepository.save(workerStats);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}