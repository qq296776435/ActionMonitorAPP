package jnu.action3;

class Action {
    private int id;
    private String name;
    private int duration;

    public Action(int id, String name, int duration) {
        super();
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration(){
        return duration;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
