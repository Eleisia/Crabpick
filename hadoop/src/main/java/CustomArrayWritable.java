import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class CustomArrayWritable extends ArrayWritable {

    public CustomArrayWritable() {
        super(Text.class);
    }

    public CustomArrayWritable(Text[] values) {
        super(Text.class, values);
    }

    public CustomArrayWritable(Writable[] values) {
        super(Text.class, values);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Writable value : get()) {
            sb.append(value.toString()).append("\t");
        }
        return sb.toString();
    }
}
