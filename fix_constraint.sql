-- =====================================================
-- FIX CHECK CONSTRAINT untuk kolom status
-- Jalankan script ini di SSMS untuk hapus constraint yang membatasi
-- =====================================================

USE CafeFlowDB;
GO

-- Cari nama constraint
DECLARE @constraintName NVARCHAR(200);

SELECT @constraintName = name
FROM sys.check_constraints
WHERE parent_object_id = OBJECT_ID('orders')
AND name LIKE '%status%';

-- Drop constraint jika ada
IF @constraintName IS NOT NULL
BEGIN
    DECLARE @sql NVARCHAR(500);
    SET @sql = 'ALTER TABLE orders DROP CONSTRAINT ' + @constraintName;
    EXEC sp_executesql @sql;
    PRINT '✓ Constraint "' + @constraintName + '" berhasil dihapus!';
END
ELSE
BEGIN
    PRINT 'ℹ No status constraint found.';
END
GO

-- Verifikasi
PRINT '';
PRINT '========================================';
PRINT 'Constraint Check FIXED!';
PRINT '========================================';
PRINT '';
PRINT 'Sekarang kolom status bisa menerima nilai apapun:';
PRINT '  - PROCESSING';
PRINT '  - COMPLETED';
PRINT '  - CANCELLED';
PRINT '  - dll.';
PRINT '';
GO