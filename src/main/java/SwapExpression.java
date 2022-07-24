import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a combination of ShiftSwaps that is only satisfied if the component swaps are performed
 * in a specific combination.
 */
public interface SwapExpression
{
    //expands the expression into a disjunction of conjunctions of ShiftSwaps
    Iterable<LinkedList<ShiftSwap>> expand();
}

class LinkedList<T> implements Iterable<T>
{
    static class Node<T>
    {
        T elem;
        Node<T> next;

        private Node(T elem) {
            this.elem = elem;
        }
    }

    Node<T> head;
    Node<T> last;

    public LinkedList() { }

    public LinkedList(T elem) {
        this();
        this.add(elem);
    }

    public boolean add(T elem)
    {
        if (head == null) {
            head = new Node<>(elem);
            last = head;
        }
        else {
            last.next = new Node<>(elem);
            last = last.next;
        }

        return true;
    }

    //returns what used to be the last node of this
    //after the call, messing with list l is undefined behaviour
    public Node<T> merge(LinkedList<T> l)
    {
        if (this.head == null)
            this.head = l.head;
        else
            this.last.next = l.head;

        Node<T> r = this.last;
        this.last = l.last;
        return r;
    }

    //n becomes the last node of this.
    //n can be null. in that case this becomes an empty list
    //undefined behaviour if n isn't a node of this
    public void split(Node<T> n)
    {
        this.last = n;

        if (n == null)
            this.head = null;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>() {
            private Node<T> current = LinkedList.this.head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T ans = current.elem;
                current = current.next;
                return ans;
            }
        };
    }

    public T[] toArray(Class<T> clazz)
    {
        ArrayList<T> list = new ArrayList<>();

        for (Node<T> n = this.head; n != null; n = n.next)
            list.add(n.elem);

        @SuppressWarnings("unchecked")
        T[] ans = (T[])Array.newInstance(clazz, list.size());
        list.toArray(ans);
        return ans;
    }
}

/**
 * Represents a single shift swap
 * Ex: Analise_TP1 -> Analise_TP2
 */
class ShiftSwap implements SwapExpression
{
    public final String from;
    public final String to;

    ShiftSwap(String from, String to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public Iterable<LinkedList<ShiftSwap>> expand() {
        return new Iterable<LinkedList<ShiftSwap>>() {
            @Override
            public Iterator<LinkedList<ShiftSwap>> iterator() {
                return new Iterator<LinkedList<ShiftSwap>>() {
                    private boolean extracted = false;

                    @Override
                    public boolean hasNext() {
                        return !extracted;
                    }

                    @Override
                    public LinkedList<ShiftSwap> next() {
                        extracted = true;
                        LinkedList<ShiftSwap> ans = new LinkedList<ShiftSwap>();
                        ans.add(ShiftSwap.this);
                        return ans;
                    }
                };
            }
        };
    }

    @Override
    public String toString() {
        return this.from.concat(" -> ").concat(this.to);
    }

    public boolean equals(ShiftSwap s) {
        return this.from.equals(s.from) && this.to.equals(s.to);
    }

    public boolean reverses(ShiftSwap s) {
        return this.from.equals(s.to) && this.to.equals(s.from);
    }

    //returns true if the swaps are mutually exclusive (i.e. they are both swaps in the same class to different shifts)
    public boolean excludes(ShiftSwap s) {
        //TODO
        return true;
    }
}

/**
 * Represents a set of swap expressions that only make the whole expression true if performed simultaneously
 * Ex: (Analise_TP1 -> Analise_TP2, LI2_TP3 -> LI2_TP4)
 */
class SwapANDConcat implements SwapExpression
{
    private SwapExpression[] list;

    private class CustomIterator implements Iterator<LinkedList<ShiftSwap>>
    {
        private Iterator<LinkedList<ShiftSwap>>[] iterators;
        private LinkedList.Node<ShiftSwap>[] breakpoints;
        private LinkedList<ShiftSwap> ans;
        private boolean hasNext;

        public CustomIterator()
        {
            iterators = new Iterator[list.length];
            breakpoints = new LinkedList.Node[list.length];
            ans = new LinkedList<>();
            hasNext = true;

            for (int i = 0; i < list.length; i++)
            {
                iterators[i] = list[i].expand().iterator();

                if (!iterators[i].hasNext()) {
                    hasNext = false;
                    break;
                }

                breakpoints[i] = ans.merge(iterators[i].next());
            }
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public LinkedList<ShiftSwap> next()
        {
            int i = iterators.length - 1;

            for (; i >= 0 && !iterators[i].hasNext(); i--)
                iterators[i] = list[i].expand().iterator();

            ans.split(breakpoints[i]);

            for (; i < iterators.length; i++)
                ans.merge(iterators[i].next());

            return ans;
        }
    }

    @Override
    public Iterable<LinkedList<ShiftSwap>> expand()
    {
        return new Iterable() {
            @Override
            public Iterator<LinkedList<ShiftSwap>> iterator() {
                return new CustomIterator();
            }
        };
    }

    @Override
    public String toString()
    {
        StringBuilder ans = new StringBuilder();
        if (list.length > 1)
            ans.append("(");

        boolean first = true;
        for (SwapExpression e : this.list)
        {
            if (!first)
                ans.append(", ");
            else
                first = false;

            ans.append(e.toString());
        }

        if (list.length > 1)
            ans.append(")");

        return ans.toString();
    }
}

/**
 * Represents a set of swap expressions that only make the whole expression true if one (and no more) is performed
 * Ex: (Analise_TP1 -> Analise_TP2 | LI2_TP3 -> LI2_TP4)
 */
class SwapXORConcat implements SwapExpression
{
    private SwapExpression[] list;

    private class CustomIterator implements Iterator<LinkedList<ShiftSwap>>
    {
        private Iterator<LinkedList<ShiftSwap>> iterator;
        private int i = 0;

        public CustomIterator() {
            if (list.length == 0)
                this.iterator = null;
            else
                this.iterator = list[0].expand().iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator != null && iterator.hasNext();
        }

        @Override
        public LinkedList<ShiftSwap> next()
        {
            LinkedList<ShiftSwap> ans = iterator.next();

            if (!iterator.hasNext()) {
                if (++i == list.length)
                    iterator = null;
                else
                    iterator = list[i].expand().iterator();
            }

            return ans;
        }
    }

    @Override
    public Iterable<LinkedList<ShiftSwap>> expand() {
        return new Iterable() {
            @Override
            public Iterator<LinkedList<ShiftSwap>> iterator() {
                return new CustomIterator();
            }
        };
    }

    @Override
    public String toString()
    {
        StringBuilder ans = new StringBuilder();
        if (list.length > 1)
            ans.append("(");

        boolean first = true;
        for (SwapExpression e : this.list)
        {
            if (!first)
                ans.append(" | ");
            else
                first = false;

            ans.append(e.toString());
        }

        if (list.length > 1)
            ans.append(")");

        return ans.toString();
    }
}