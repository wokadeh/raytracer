package scene;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Scene {

    private List<IShape> mShapeList;

    public List<IShape> getShapeList() {
        return mShapeList;
    }

    public Scene(){
        mShapeList = new List<IShape>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<IShape> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(IShape iShape) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends IShape> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends IShape> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean equals(Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public IShape get(int index) {
                return null;
            }

            @Override
            public IShape set(int index, IShape element) {
                return null;
            }

            @Override
            public void add(int index, IShape element) {

            }

            @Override
            public IShape remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<IShape> listIterator() {
                return null;
            }

            @Override
            public ListIterator<IShape> listIterator(int index) {
                return null;
            }

            @Override
            public List<IShape> subList(int fromIndex, int toIndex) {
                return null;
            }
        };

    }
}
