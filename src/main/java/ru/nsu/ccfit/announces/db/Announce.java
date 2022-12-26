package ru.nsu.ccfit.announces.db;

import ru.nsu.ccfit.announces.subject.Subject;

import java.sql.Date;

public class Announce {
    public int id;
    public String text;
    public Integer[] mediaIds;
    public Date publishDate;
    public Date creationDate;
    public Date lastEditDate;
    public Integer[] subjectsIds;
}
