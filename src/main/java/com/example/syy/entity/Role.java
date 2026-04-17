package com.example.syy.entity;

public class Role {
    private int rid;
    private String rname;
    private String rdscpt;

    public Role() {
    }

    public Role(int rid, String rname, String rdscpt) {
        this.rid = rid;
        this.rname = rname;
        this.rdscpt = rdscpt;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getRdscpt() {
        return rdscpt;
    }

    public void setRdscpt(String rdscpt) {
        this.rdscpt = rdscpt;
    }
}
