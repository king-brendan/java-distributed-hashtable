package MessageTypes;

import java.io.Serializable;

public interface Message extends Serializable {
    public String type();
}
