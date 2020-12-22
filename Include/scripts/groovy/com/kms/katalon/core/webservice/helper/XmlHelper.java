package com.kms.katalon.core.webservice.helper;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public final class XmlHelper {
    private XmlHelper() {
    }

    public static Collection<Node> wrapNodeList( final NodeList nodeList ) throws IllegalArgumentException {
        if ( nodeList == null )
            throw new IllegalArgumentException( "Cannot wrap null NodeList" );

        return new Collection<Node>() {
            @Override
            public int size() {
                return nodeList.getLength();
            }

            @Override
            public boolean isEmpty() {
                return nodeList.getLength() > 0;
            }

            @Override
            public boolean contains( final Object o ) {
                if ( o == null || !( o instanceof Node ) )
                    return false;
                for ( int i = 0; i < nodeList.getLength(); ++i )
                    if ( o == nodeList.item( i ) )
                        return true;
                return false;
            }

            @Override
            public Iterator<Node> iterator() {
                return new Iterator<Node>() {
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return nodeList.getLength() > this.index;
                    }

                    @Override
                    public Node next() {
                        if ( this.index >= nodeList.getLength() )
                            throw new NoSuchElementException();
                        return nodeList.item( this.index++ );
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public Object[] toArray() {
                final Node[] array = new Node[nodeList.getLength()];
                for ( int i = 0; i < array.length; ++i )
                    array[ i ] = nodeList.item( i );
                return array;
            }

            @Override
            @SuppressWarnings( { "unchecked" } )
            public <T> T[] toArray( final T[] a ) throws ArrayStoreException {
                if ( !a.getClass().getComponentType().isAssignableFrom( Node.class ) )
                    throw new ArrayStoreException(
                            a.getClass().getComponentType().getName() + " is not the same or a supertype of Node" );

                if ( a.length >= nodeList.getLength() ) {
                    for ( int i = 0; i < nodeList.getLength(); ++i )
                        a[ i ] = (T) nodeList.item( i );
                    if ( a.length > nodeList.getLength() )
                        a[ nodeList.getLength() ] = null;
                    return a;
                }

                return (T[]) toArray();
            }

            @Override
            public boolean add( final Node node ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean remove( final Object o ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean containsAll( final Collection<?> c ) {
                for ( final Object o : c )
                    if ( !this.contains( o ) )
                        return false;
                return true;
            }

            @Override
            public boolean addAll( final Collection<? extends Node> c ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean removeAll( final Collection<?> c ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean retainAll( final Collection<?> c ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void clear() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public static String prettyFormat(String input, int indent) {
        try
        {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            // This statement works with JDK 6
            transformerFactory.setAttribute("indent-number", indent);
             
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        }
        catch (Throwable e)
        {
            // You'll come here if you are using JDK 1.5
            // you are getting an the following exeption
            // java.lang.IllegalArgumentException: Not supported: indent-number
            // Use this code (Set the output property in transformer.
            try
            {
                Source xmlInput = new StreamSource(new StringReader(input));
                StringWriter stringWriter = new StringWriter();
                StreamResult xmlOutput = new StreamResult(stringWriter);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
                transformer.transform(xmlInput, xmlOutput);
                return xmlOutput.getWriter().toString();
            }
            catch(Throwable t)
            {
                return input;
            }
        }
    }
 
    public static String prettyFormat(String input) {
        return prettyFormat(input, 2);
    }
}