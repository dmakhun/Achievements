package com.softserve.edu.manager;

import com.softserve.edu.dao.GroupDao;
import com.softserve.edu.dao.ScheduleDao;
import com.softserve.edu.dao.ScheduleGroupDao;
import com.softserve.edu.entity.Group;
import com.softserve.edu.entity.Schedule;
import com.softserve.edu.entity.ScheduleTable;
import com.softserve.edu.util.CsvParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * @author edgar
 */
@Service("scheduleManager")
public class ScheduleManagerImplementation implements ScheduleManager {

    @Autowired
    ScheduleDao scheduleDao;

    @Autowired
    GroupDao groupDao;

    @Override
    public Map<Long, String> table(Calendar calendar, String group) {
        Long placeOfText = 0l;
        Map<Long, String> map = new LinkedHashMap<Long, String>();
        ScheduleRowsManagerImplementation sr = new ScheduleRowsManagerImplementation(
                calendar);
        List<Calendar> calen = sr.getWeek();
        ScheduleGroupDao sgd = new ScheduleGroupDao(group);
        List<Schedule> schList = sgd.listScheduleForGroup;
        for (Calendar c : calen) {
            ++placeOfText;
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);
            int h = c.get(Calendar.HOUR_OF_DAY);

            for (Schedule s : schList) {
                if (s.getStartDateAndTime().get(Calendar.MONTH) == m
                        && s.getStartDateAndTime().get(Calendar.DAY_OF_MONTH) == d
                        && s.getStartDateAndTime().get(Calendar.HOUR_OF_DAY) == h) {

                    map.put(placeOfText,
                            s.getMeetType() + " " + s.getLocation());
                    Calendar calendarTemp = s.getStartDateAndTime();
                    calendarTemp.add(Calendar.HOUR_OF_DAY, 1);

                    Long temp = placeOfText;
                    while (calendarTemp.before(s.getEndDateAndTime())) {
                        map.put(++temp, s.getMeetType() + " " + s.getLocation());
                        calendarTemp.add(Calendar.HOUR_OF_DAY, 1);
                    }

                }

            }
        }
        return map;
    }

    @Override
    public File saveFileOnServer(MultipartFile file) throws Exception {
        File serverFile = null;
        if (file.isEmpty()) {
            throw new Exception("File is empty");
        }
        byte[] bytes = file.getBytes();
        // Creating the directory to store file
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "tmpFiles");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Random random = new Random();
        int i = random.nextInt();
        if (i < 0) {
            i = i * (-1);
        }
        File finalFile = new File(rootPath + File.separator + "CSV"
                + File.separator + i + ".csv");
        while (finalFile.exists()) {
            i = random.nextInt();
            if (i < 0) {
                i = i * (-1);
            }
        }

        serverFile = new File(dir.getAbsolutePath() + File.separator + i
                + ".csv");
        BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();

        if (!isUnique(serverFile)) {
            throw new Exception("This file was added before");
        }
        try {
            FileUtils.copyFile(serverFile, finalFile);
        } finally {
            serverFile.delete();
        }
        return finalFile;
    }

    @SuppressWarnings("resource")
    private boolean isUnique(File file) throws IOException {
        String rootPath = System.getProperty("catalina.home");
        File[] files = new File(rootPath + File.separator + "CSV").listFiles();

        InputStream input1 = new FileInputStream(file);
        InputStream input2 = null;

        for (File f : files) {
            input2 = new FileInputStream(f);

            if (IOUtils.contentEquals(input1, input2)) {
                return false;
            }
        }

        return true;

    }

    @Override
    @Transactional
    public void fillDBfromCSV(File file) throws Exception {
        int count = 1;
        try {
            List<Schedule> listSchedules = new CsvParser().mapToCSV(file);
            Group generalGroup = null;
            for (Object object : listSchedules) {
                Schedule schedule = (Schedule) object;

                if (generalGroup != null
                        && schedule.getGroup().equals(generalGroup.getName())) {
                    ScheduleTable scheduleTable = new ScheduleTable(
                            generalGroup, schedule.getMeetType(),
                            schedule.getStartDateAndTime(),
                            schedule.getEndDateAndTime(),
                            schedule.getDescription(), schedule.getLocation());
                    scheduleDao.save(scheduleTable);
                    count++;
                    continue;

                }

                Group group = groupDao.findGroupByName(schedule.getGroup());
                if (group == null) {
                    group = new Group();
                    group.setName(schedule.getGroup());
                    groupDao.save(group);

                }
                generalGroup = group;
                ScheduleTable scheduleTable = new ScheduleTable(group,
                        schedule.getMeetType(), schedule.getStartDateAndTime(),
                        schedule.getEndDateAndTime(),
                        schedule.getDescription(), schedule.getLocation());
                scheduleDao.save(scheduleTable);
                count++;
            }
        } catch (Exception e) {
            file.delete();
            throw new Exception("invalid value in colum " + count);
        }
    }
}
