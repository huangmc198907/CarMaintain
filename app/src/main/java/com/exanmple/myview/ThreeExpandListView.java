package com.exanmple.myview;

import java.util.List;

public class ThreeExpandListView {
    //国家名
    private String name;
    //国家的省集合
    private List<FirstListView> FirstListViews;

    public String getName() {
        return name;
    }

    public int getSize() {
        return this.FirstListViews.size();
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FirstListView> getFirstListViews() {
        return FirstListViews;
    }

    public void setFirstListViews(List<FirstListView> FirstListViews) {
        this.FirstListViews = FirstListViews;
    }

    public static class FirstListView{
        //省的名称
        private String name;

        //省内的城市集合
        private List<SecondListView> secondList;

        public String getName() {
            return name;
        }

        public int getSize() {
            return this.secondList.size();
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<SecondListView> getSecondList() {
            return secondList;
        }

        public void setSecondList(List<SecondListView> secondList) {
            this.secondList = secondList;
        }
    }

    public static class SecondListView{
        //城市名称
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
