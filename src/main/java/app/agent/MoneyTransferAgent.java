package app.agent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import app.model.MoneyTransfer;
import app.service.MoneyTransferService;
import app.service.ServiceException;

public class MoneyTransferAgent {

	private MoneyTransferService moneyTransferService = new MoneyTransferService();

	private final ScheduledExecutorService executorService;

	public MoneyTransferAgent(int threadCount, int intervalInSeconds) {
		executorService = Executors.newScheduledThreadPool(threadCount);
		executorService.scheduleWithFixedDelay(this::run, intervalInSeconds, intervalInSeconds, TimeUnit.SECONDS);
	}

	public void run() {
		System.out.println("---> MoneyTransferAgent run() ---> ");
		for (MoneyTransfer moneyTransfer : moneyTransferService.findAllByStatus("SUBMITTED", 100)) {
			try {
				moneyTransferService.execute(moneyTransfer);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		executorService.shutdown();
		try {
			executorService.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
