import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DocumentIterator
    implements Iterator<String>
{

    private Reader r;
    private int n;
    private int c = -1;


    public DocumentIterator(Reader r, int n)
    {
        this.r = r;
        this.n = n;
        skipNonLetters();
    }


    private void skipNonLetters()
    {
        try
        {
            this.c = this.r.read();
            while (!Character.isLetter(this.c) && this.c != -1)
            {
                this.c = this.r.read();
            }
        }
        catch (IOException e)
        {
            this.c = -1;
        }
    }


    @Override
    public boolean hasNext()
    {
        return (c != -1);
    }
    
    @Override
    public String next()
    {
		int num = 0;

		if (!hasNext()) {
			throw new NoSuchElementException();
		} 
		String ans = "";
		String tmp = "";
		try {
			while (num < this.n && hasNext()) {
				tmp = ans;
				while (Character.isLetter(this.c)) {
					ans = ans + (char) this.c;
					this.c = this.r.read();
				}

				if (num == 0) {
					this.r.mark(1000);
				}
				if (!tmp.equals(ans)) {
					num++;
				}
				skipNonLetters();
			}
			this.r.reset();
			this.c = this.r.read();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			//throw new NoSuchElementException();
		} 

		if (num < this.n) {
			ans = "";
			this.c = -1;
		}

		return ans;
    }

}
