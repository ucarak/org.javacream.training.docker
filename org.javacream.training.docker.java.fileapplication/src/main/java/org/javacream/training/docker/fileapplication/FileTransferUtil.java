package org.javacream.training.docker.fileapplication;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class FileTransferUtil implements FileTransferUtilMBean {

	private int iterations;
	private int filesDeleted;

	public int getIterations() {
		return iterations;
	}

	public void reset() {
		iterations = 0;
		filesDeleted = 0;
	}

	public int getFilesDeleted() {
		return filesDeleted;
	}

	public static void main(String[] args) {
		new FileTransferUtil();
	}

	{
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		try {
			server.registerMBean(this, new ObjectName("org.javacream:service=FileUtil"));
		} catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException
				| MalformedObjectNameException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				iterations++;
				try {
					File file = new File("/input");
					String[] fileNames = file.list();
					System.out.println("input-directory: " + Arrays.asList(fileNames));
					File[] files = file.listFiles();
					for (File f : files) {
						f.delete();
					}
					filesDeleted += files.length;
				} catch (Exception e) {
					System.out.println("input-directory not found");
				}
			}

		}, 0, 5, TimeUnit.SECONDS);
	}

}
