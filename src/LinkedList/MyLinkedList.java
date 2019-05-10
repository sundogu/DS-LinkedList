package LinkedList;

import java.util.*;

public class MyLinkedList<T> implements List<T> {
    // Class Variables //
    private MyNode head = new MyNode(null);
    private MyNode tail = new MyNode(null);
    private int size;

    public MyLinkedList() {
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    public MyLinkedList(Collection<T> c) {
        this();
        this.addAll(c);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return this.indexOf(o) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyListIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] temp = new Object[size];

        int i = 0;
        for (Object el : this) {
            temp[i++] = el;
        }

        return temp;
    }

    @Override @SuppressWarnings("unchecked")
    public Object[] toArray(Object[] array) {
        array = (array.length < size)? new Object[size] : array;

        int i = 0;
        for (Object el : this) {
            array[i++] = el;
        }

        if (array.length > size) {
            array[size] = null;
        }


        return array;
    }

    @Override
    public boolean add(T t) {
        MyNode newNode = new MyNode(t);
        MyNode prevNode = tail.prev;

        prevNode.next = tail.prev = newNode;
        newNode.prev = prevNode;
        newNode.next = tail;

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o != null) {
            ListIterator itr = this.listIterator();

            while(itr.hasNext()) {
                if (itr.next().equals(o)) {
                    itr.remove();
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        HashSet<T> lookup = new HashSet<>(this);

        for (Object el : c) {
            if (!lookup.contains(el)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T el : c) {
            this.add(el);
        }

        return c.size() != 0;
    }

    @Override @SuppressWarnings("unchecked")
    public boolean addAll(int index, Collection<? extends T> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    String.format("Cannot add element to index %d from list of size %d", index, size));
        }

        if (c.size() == 1) {
            for (T el : c) {
                this.add(index, el);
            }
        } else if (c.size() > 1) {
            Iterator<T> cItr = ((Collection<T>) c).iterator();
            MyNode tempHead = new MyNode(cItr.next());
            MyNode tempTail = new MyNode(null);


            for (int i = 1; cItr.hasNext(); i++) {
                MyNode bodyNode = new MyNode(cItr.next());

                if (i == 1) {
                    bodyNode.prev = tempHead;
                    tempHead.next = bodyNode;
                } else {
                    bodyNode.prev = tempTail;
                    tempTail.next = bodyNode;
                }

                tempTail = bodyNode;
            }

            MyListIterator itr = (MyListIterator) this.listIterator(index);
            MyNode temp = itr.nextNode();
            MyNode prevNode = temp.prev;

            prevNode.next = tempHead;
            tempHead.prev = prevNode;
            temp.prev = tempTail;
            tempTail.next = temp;
            size += c.size();
        }

        return c.size() != 0;
    }

    @Override @SuppressWarnings("unchecked")
    public boolean removeAll(Collection c) {
        int initSize = size;
        HashSet<T> lookup = new HashSet<>(c);
        this.removeIf(lookup::contains);
        return size != initSize;
    }

    @Override @SuppressWarnings("unchecked")
    public boolean retainAll(Collection c) {
        int initSize = size;
        HashSet<T> lookup = new HashSet<>(c);
        this.removeIf(el -> !lookup.contains(el));
        return size != initSize;
    }

    @Override
    public void clear() {
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    String.format("Cannot get element at index %d from list of size %d", index, size));
        }

        ListIterator<T> itr = this.listIterator(index);
        return itr.next();
    }

    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    String.format("Cannot set element to index %d from list of size %d", index, size));
        }

        MyListIterator itr = (MyListIterator) this.listIterator(index);
        T el = itr.next();
        itr.set(element);

        return el;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    String.format("Cannot add element to index %d from list of size %d", index, size));
        }

        MyListIterator itr = (MyListIterator) this.listIterator(index);
        itr.add(element);
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    String.format("Cannot remove element at index %d from list of size %d", index, size));
        }

        ListIterator<T> itr = this.listIterator(index);
        T t = itr.next();
        itr.remove();

        return t;
    }

    @Override
    public int indexOf(Object o) {
        if (o != null) {
            int i = 0;

            for (T el : this) {
                if (el.equals(o)) {
                    return i;
                }

                i++;
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o != null && size != 0) {
            MyListIterator itr = (MyListIterator) this.listIterator(size - 1);

            if (itr.next().equals(o)) {
                return itr.index - 1;
            }

            while (itr.hasPrevious()) {
                if (itr.previous().equals(o)) {
                    return itr.index - 1;
                }
            }
        }

        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new MyListIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    String.format("Cannot iterate element at index %d from list of size %d", index, size));
        }

        return new MyListIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        List<T> temp = new MyLinkedList<T>();
        ListIterator<T> itr = this.listIterator(fromIndex);

        while (itr.nextIndex() < toIndex) {
            temp.add(itr.next());
        }


        return temp;
    }

    private class MyNode {
        // Class Variables //
        private T element;
        private MyNode next;
        private MyNode prev;

        // Constructors //
        private MyNode(T element) {
            this.element = element;
        }
    }

    private class MyListIterator implements ListIterator<T> {
        // Class Variables //
        /**
         * The cursor pointing at the current node
         */
        private MyNode currNode;
        /**
         * The current index
         */
        private int index;
        /**
         * Flag specifying if the list can be modified at the moment
         */
        private boolean canModify;

        // Constructors //
        private MyListIterator() {
            currNode = head;
            index = 0;
            canModify = false;
        }

        private MyListIterator(int index) {
            MyListIterator itr = new MyListIterator();

            for (int i = 0; i < index; i++) {
                itr.next();
            }

            currNode = itr.currNode;
            this.index = itr.index;
            canModify = false;
        }

        // Methods //
        @Override
        public boolean hasNext() {
            return !currNode.next.equals(tail);
        }

        @Override
        public T next() {
            if (currNode.next.equals(tail)) {
                throw new IndexOutOfBoundsException();
            }

            canModify = true;
            index++;
            return (currNode = currNode.next).element;
        }

        private MyNode nextNode() {
            if (currNode.next.equals(tail)) {
                throw new IndexOutOfBoundsException();
            }

            canModify = true;
            index++;
            return (currNode = currNode.next);
        }

        @Override
        public boolean hasPrevious() {
            return !currNode.prev.equals(head);
        }

        @Override
        public T previous() {
            if (currNode.prev.equals(head)) {
                throw new IndexOutOfBoundsException();
            }

            canModify = true;
            index--;
            return (currNode = currNode.prev).element;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            if (!canModify) {
                throw new IllegalStateException("Cannot remove element without first calling next/prev");
            }

            MyNode nextNode = currNode.next;
            MyNode prevNode = currNode.prev;

            nextNode.prev = prevNode;
            prevNode.next = nextNode;
            canModify = false;
            currNode = prevNode;
            index--;
            size--;
        }

        @Override
        public void set(T t) {
            if (!canModify) {
                throw new IllegalStateException(
                        "Cannot set element if add/remove has been called after the last call to next/prev");
            }

            currNode.element = t;
        }

        @Override
        public void add(T t) {
            MyNode newNode = new MyNode(t);
            MyNode nextNode = currNode.next;

            currNode.next = nextNode.prev = newNode;
            newNode.prev = currNode;
            newNode.next = nextNode;

            currNode = newNode;
            size++;
            index++;
            canModify = false;
        }
    }
}
