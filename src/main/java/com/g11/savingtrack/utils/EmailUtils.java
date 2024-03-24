package com.g11.savingtrack.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

// Class
public class EmailUtils {

    // Class data members
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
