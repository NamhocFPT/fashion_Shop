package model;

import entity.Categories;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.sql.*;
import java.util.Vector;

public class DAOCategoriesTest {

    private DAOCategories dao;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private Statement mockStatement;
    private ResultSet mockResultSet;

    @Before
    public void setUp() throws SQLException {
        // Mock các đối tượng liên quan đến kết nối cơ sở dữ liệu
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);

        // Khởi tạo đối tượng DAO và mock kết nối
        dao = new DAOCategories();
        dao.conn = mockConnection;
    }

    @Test
    public void testAddCategory() throws SQLException {
        // Tạo một đối tượng Category
        Categories category = new Categories(1, "Electronics");

        // Mock phương thức prepareStatement để trả về PreparedStatement giả
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Mock kết quả executeUpdate khi gọi phương thức này
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Giả sử insert thành công

        // Kiểm tra phương thức addCategory
        int result = dao.addCategory(category);

        // Kiểm tra kết quả trả về
        assertEquals(1, result);

        // Kiểm tra xem phương thức executeUpdate có được gọi không
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testGetCategories() throws SQLException {
        // Mock các hành động liên quan đến Statement và ResultSet
        when(mockConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE))
                .thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        // Giả sử có một kết quả trả về từ ResultSet
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // Chỉ có 1 dòng trả về
        when(mockResultSet.getInt(1)).thenReturn(1);
        when(mockResultSet.getString(2)).thenReturn("Electronics");

        // Gọi phương thức getCategories
        Vector<Categories> categoriesList = dao.getCategories("SELECT * FROM [dbo].[Categories]");

        // Kiểm tra kết quả trả về
        assertNotNull(categoriesList);
        assertEquals(1, categoriesList.size());
        assertEquals("Electronics", categoriesList.get(0).getCategoryName());

        // Kiểm tra xem kết quả trả về từ ResultSet có hợp lý không
        verify(mockResultSet).next();
    }

    @Test
    public void testMain() {
        // Kiểm tra phương thức main
        // Do phương thức main chỉ in ra danh sách categories, không có hành động trả về cụ thể, 
        // nên chúng ta có thể chỉ gọi main và đảm bảo rằng không xảy ra lỗi.
        try {
            DAOCategories.main(new String[] {});
        } catch (Exception e) {
            fail("No exception should be thrown");
        }
    }
}
