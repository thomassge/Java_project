package data;

import java.util.ArrayList;

public abstract class Objects {

    protected int localObjectCount;
    protected int serverObjectCount;
    protected int memoryObjectCount;

    public abstract ArrayList<?> initialise(String jsonString);
    public abstract void update();

}
