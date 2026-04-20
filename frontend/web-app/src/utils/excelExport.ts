import * as XLSX from 'xlsx';
import { saveAs} from 'file-saver';

export const exportToExcel = (data: any[], fileName: string) => {
  // 1. Create a new worksheet from the JSON data
  const worksheet = XLSX.utils.json_to_sheet(data);
  
  // 2. Create a new workbook and append the worksheet
  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, "Inventory");

  // 3. Generate the Excel file buffer
  const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });

  // 4. Create a Blob and trigger the download
  const blob = new Blob([excelBuffer], { 
    type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8' 
  });
  saveAs(blob, `${fileName}.xlsx`);
};
