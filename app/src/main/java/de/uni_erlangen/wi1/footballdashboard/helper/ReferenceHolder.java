package de.uni_erlangen.wi1.footballdashboard;

/**
 * Created by knukro on 6/28/17.
 */

public class ReferenceHolder<T>
{

    public T val;

    public ReferenceHolder(T val)
    {
        this.val = val;
    }

    public ReferenceHolder()
    {
        // Empty
    }

}
