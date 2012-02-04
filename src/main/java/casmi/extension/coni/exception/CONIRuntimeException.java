/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 *  casmi is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package casmi.extension.coni.exception;

import casmi.exception.CasmiRuntimeException;

public class CONIRuntimeException extends CasmiRuntimeException {

    public CONIRuntimeException() {
        super();
    }

    public CONIRuntimeException(String message) {
        super(message);
    }
    
    public CONIRuntimeException(Throwable cause) {
        super(cause);
    }
}
