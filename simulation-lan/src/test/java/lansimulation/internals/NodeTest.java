package lansimulation.internals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeTest {

    @Test
    public void testAttributes() {
        NetworkElement nextElement = Mockito.mock(NetworkElement.class);
        String expectedName = "NodeName";
        Node node = new Node(expectedName, nextElement);
        assertEquals(expectedName, node.name, "Name is not as expected");
        assertEquals(nextElement, node.getNextElement(), "NextElement is not set as expected");
    }

}