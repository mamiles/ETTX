//**************************************************
// Copyright (c) 2001, 2002 Cisco Systems, Inc.
// All rights reserved.
//**************************************************
// Author: Marvin Miles

package com.cisco.ettx.admin.common.net;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.awt.Dimension;

import java.lang.Byte;
/**
 * This is a telnet protocol handler. The handler needs implementations
 * for several methods to handle the telnet options and to be able to
 * read and write the buffer.
 */
public abstract class TelnetProtocolHandler {
  /** contains the current revision id */
  public final static String ID = "$Id: TelnetProtocolHandler.java,v 2.17 2002/05/06 07:24:20 leo Exp $";
  
  /** debug level */
  private final static int debug = 10;
  private static Logger logger = Logger.getLogger(TelnetProtocolHandler.class);

  /** temporary buffer for data-telnetstuff-data transformation */
  private byte[] tempbuf = new byte[0];

  /** the data sent on pressing <RETURN>  \n */
  private byte[] crlf = new byte[2];
  /** the data sent on pressing <LineFeed>  \r */
  private byte[] cr = new byte[2];

  /**
   * Create a new telnet protocol handler.
   */
  public TelnetProtocolHandler() {
    reset();

    crlf[0] = 13; crlf[1] = 10;
    cr[0] = 13; cr[1] = 0;
  }

  /**
   * Get the current terminal type for TTYPE telnet option.
   * @return the string id of the terminal
   */
  protected abstract String getTerminalType(); 

  /**
   * Get the current window size of the terminal for the
   * NAWS telnet option.
   * @return the size of the terminal as Dimension
   */
  protected abstract Dimension getWindowSize();

  /**
   * Set the local echo option of telnet.
   * @param echo true for local echo, false for no local echo
   */
  protected abstract void setLocalEcho(boolean echo);

  /**
   * Generate an EOR (end of record) request. For use by prompt displaying.
   */
  protected abstract void notifyEndOfRecord();

  /**
   * Send data to the remote host.
   * @param b array of bytes to send
   */
  protected abstract void write(byte[] b) throws IOException;

  /**
   * Send one byte to the remote host.
   * @param b the byte to be sent
   * @see #write(byte[] b)
   */
  private static byte[] one = new byte[1];
  private void write(byte b) throws IOException {
    one[0] = b;
    write(one);
  }

  /**
   * Reset the protocol handler. This may be necessary after the
   * connection was closed or some other problem occured.
   */
  public void reset() {
    neg_state = 0;
    receivedDX = new byte[256]; 
    sentDX = new byte[256];
    receivedWX = new byte[256]; 
    sentWX = new byte[256];
  }

  // ===================================================================
  // the actual negotiation handling for the telnet protocol follows:
  // ===================================================================

  /** state variable for telnet negotiation reader */
  private byte neg_state = 0;

  /** constants for the negotiation state */
  private final static byte STATE_DATA  = 0;
  private final static byte STATE_IAC   = 1;
  private final static byte STATE_IACSB = 2;
  private final static byte STATE_IACWILL       = 3;
  private final static byte STATE_IACDO = 4;
  private final static byte STATE_IACWONT       = 5;
  private final static byte STATE_IACDONT       = 6;
  private final static byte STATE_IACSBIAC      = 7;
  private final static byte STATE_IACSBDATA     = 8;
  private final static byte STATE_IACSBDATAIAC  = 9;

  /** What IAC SB <xx> we are handling right now */
  private byte current_sb;

  /** IAC - init sequence for telnet negotiation. */
  private final static byte IAC  = (byte)255;
  /** [IAC] End Of Record */
  private final static byte EOR  = (byte)239;
  /** [IAC] WILL */
  private final static byte WILL  = (byte)251;
  /** [IAC] WONT */
  private final static byte WONT  = (byte)252;
  /** [IAC] DO */
  private final static byte DO    = (byte)253;
  /** [IAC] DONT */
  private final static byte DONT  = (byte)254;
  /** [IAC] Sub Begin */
  private final static byte SB  = (byte)250;
  /** [IAC] Sub End */
  private final static byte SE  = (byte)240;
  /** Telnet option: binary mode */
  private final static byte TELOPT_BINARY= (byte)0;  /* binary mode */
  /** Telnet option: echo text */
  private final static byte TELOPT_ECHO  = (byte)1;  /* echo on/off */
  /** Telnet option: sga */
  private final static byte TELOPT_SGA   = (byte)3;  /* supress go ahead */
  /** Telnet option: End Of Record */
  private final static byte TELOPT_EOR   = (byte)25;  /* end of record */
  /** Telnet option: Negotiate About Window Size */
  private final static byte TELOPT_NAWS  = (byte)31;  /* NA-WindowSize*/
  /** Telnet option: Terminal Type */
  private final static byte TELOPT_TTYPE  = (byte)24;  /* terminal type */

  private final static byte[] IACWILL  = { IAC, WILL };
  private final static byte[] IACWONT  = { IAC, WONT };
  private final static byte[] IACDO    = { IAC, DO      };
  private final static byte[] IACDONT  = { IAC, DONT };
  private final static byte[] IACSB  = { IAC, SB };
  private final static byte[] IACSE  = { IAC, SE };

  /** Telnet option qualifier 'IS' */
  private final static byte TELQUAL_IS = (byte)0;
  /** Telnet option qualifier 'SEND' */
  private final static byte TELQUAL_SEND = (byte)1;

  /** What IAC DO(NT) request do we have received already ? */
  private byte[] receivedDX;
  /** What IAC WILL/WONT request do we have received already ? */
  private byte[] receivedWX;
  /** What IAC DO/DONT request do we have sent already ? */
  private byte[] sentDX;
  /** What IAC WILL/WONT request do we have sent already ? */
  private byte[] sentWX;

  /**
   * Send a Telnet Escape character (IAC <code>)
   */
  public void sendTelnetControl(byte code)
    throws IOException {
    byte[] b = new byte[2];

    b[0] = IAC;
    b[1] = code;
    write(b);
  }


  /**
   * Handle an incoming IAC SB &lt;type&gt; &lt;bytes&gt; IAC SE
   * @param type type of SB
   * @param sbata byte array as &lt;bytes&gt;
   * @param sbcount nr of bytes. may be 0 too.
   */
  private void handle_sb(byte type, byte[] sbdata, int sbcount) 
    throws IOException {
    if(debug > 1) 
      logger.debug("TelnetIO.handle_sb("+type+")");
    switch (type) {
    case TELOPT_TTYPE:
      if (sbcount>0 && sbdata[0]==TELQUAL_SEND) {
        write(IACSB);write(TELOPT_TTYPE);write(TELQUAL_IS);
        /* FIXME: need more logic here if we use 
         * more than one terminal type
         */
        String ttype = getTerminalType();
        if(ttype == null) ttype = "dumb";
        write(ttype.getBytes());
        write(IACSE);
      }

    }
  }

  /**
   * Do not send any notifications at startup. We do not know,
   * whether the remote client understands telnet protocol handling,
   * so we are silent.
   * (This used to send IAC WILL SGA, but this is false for a compliant
   *  client.)
   */
  public void startup() throws IOException {
  }
  /**
   * Transpose special telnet codes like 0xff or newlines to values
   * that are compliant to the protocol. This method will also send
   * the buffer immediately after transposing the data.
   * @param buf the data buffer to be sent
   */
  public void transpose(byte[] buf) throws IOException {
    int i;
    
    byte[] nbuf,xbuf;
    int nbufptr=0;
    nbuf = new byte[buf.length*2]; // FIXME: buffer overflows possible

    for (i = 0; i < buf.length ; i++) {
      switch (buf[i]) {
      // Escape IAC twice in stream ... to be telnet protocol compliant
      // this is there in binary and non-binary mode.
      case IAC:
	nbuf[nbufptr++]=IAC;
	nbuf[nbufptr++]=IAC;
	break;
      // We need to heed RFC 854. LF (\n) is 10, CR (\r) is 13
      // we assume that the Terminal sends \n for lf+cr and \r for just cr
      // linefeed+carriage return is CR LF */ 
      case 10:	// \n
        if (receivedDX[TELOPT_BINARY + 128 ] != DO) {
	    while (nbuf.length - nbufptr < crlf.length) {
		    xbuf = new byte[nbuf.length*2];
		    System.arraycopy(nbuf,0,xbuf,0,nbufptr);
		    nbuf = xbuf;
	    }
	    for (int j=0;j<crlf.length;j++)
		nbuf[nbufptr++]=crlf[j];
	    break;
	} else {
	    // copy verbatim in binary mode.
	    nbuf[nbufptr++]=buf[i];
	}
	break;
      // carriage return is CR NUL */ 
      case 13:	// \r
        if (receivedDX[TELOPT_BINARY + 128 ] != DO) {
	    while (nbuf.length - nbufptr < cr.length) {
		    xbuf = new byte[nbuf.length*2];
		    System.arraycopy(nbuf,0,xbuf,0,nbufptr);
		    nbuf = xbuf;
	    }
	    for (int j=0;j<cr.length;j++)
		nbuf[nbufptr++]=cr[j];
	} else {
	    // copy verbatim in binary mode.
	    nbuf[nbufptr++]=buf[i];
	}
	break;
      // all other characters are just copied
      default:
	nbuf[nbufptr++]=buf[i];
	break;
      }
    }
    xbuf = new byte[nbufptr];
    System.arraycopy(nbuf,0,xbuf,0,nbufptr);
    write(xbuf);
  }

  public void setCRLF(String xcrlf) { crlf = xcrlf.getBytes(); }
  public void setCR(String xcr) { cr = xcr.getBytes(); }

  /**
   * Handle telnet protocol negotiation. The buffer will be parsed
   * and necessary actions are taken according to the telnet protocol.
   * See <A HREF="RFC-Telnet-URL">RFC-Telnet</A>
   * @param buf the byte buffer used for negotiation
   * @param count the amount of bytes in the buffer
   * @return a new buffer after negotiation
   */
  public int negotiate(byte nbuf[])
  throws IOException
  {
    byte sbbuf[] = new byte[tempbuf.length];
    int count = tempbuf.length;
    byte[] buf = tempbuf;
    byte sendbuf[] = new byte[3];
    byte b,reply;
    int  sbcount = 0;
    int boffset = 0, noffset = 0;
    boolean dobreak = false;

    if (count == 0) 	// buffer is empty.
      return -1;

    while(!dobreak && (boffset < count) && (noffset < nbuf.length)) {
      b=buf[boffset++];
      // of course, byte is a signed entity (-128 -> 127)
      // but apparently the SGI Netscape 3.0 doesn't seem
      // to care and provides happily values up to 255
      if (b>=128)
        b=(byte)((int)b-256);
//    if(debug > 2) {
//      Byte B = new Byte(b);
//      logger.debug("byte: " + B.intValue()+ " ");
//    }
      switch (neg_state) {
      case STATE_DATA:
        if (b==IAC) {
          neg_state = STATE_IAC;
	  dobreak = true; // leave the loop so we can sync.
        } else 
          nbuf[noffset++]=b;
        break;
      case STATE_IAC:
        switch (b) {
        case IAC:
          if(debug > 2) logger.debug("IAC ");
          neg_state = STATE_DATA;
          nbuf[noffset++]=IAC;
          break;
        case WILL:
          if(debug > 2) logger.debug("WILL ");
          neg_state = STATE_IACWILL;
          break;
        case WONT:
          if(debug > 2) logger.debug("WONT ");
          neg_state = STATE_IACWONT;
          break;
        case DONT:
          if(debug > 2) logger.debug("DONT ");
          neg_state = STATE_IACDONT;
          break;
        case DO:
          if(debug > 2) logger.debug("DO ");
          neg_state = STATE_IACDO;
          break;
        case EOR:
          if(debug > 1) logger.debug("EOR ");
  	  notifyEndOfRecord();
	  dobreak = true; // leave the loop so we can sync.
          neg_state = STATE_DATA;
          break;
        case SB:
          if(debug > 2) logger.debug("SB ");
          neg_state = STATE_IACSB;
          sbcount = 0;
          break;
        default:
          if(debug > 2) logger.debug("<UNKNOWN "+b+" > ");
          neg_state = STATE_DATA;
          break;
        }
        break;
      case STATE_IACWILL:
        switch(b) {
        case TELOPT_ECHO:
          if(debug > 2) logger.debug("ECHO");
          reply = DO;
          setLocalEcho(false);
          break;
        case TELOPT_SGA:
          if(debug > 2) logger.debug("SGA");
          reply = DO;
          break;
        case TELOPT_EOR:
          if(debug > 2) logger.debug("EOR");
          reply = DO;
          break;
        case TELOPT_BINARY:
          if(debug > 2) logger.debug("BINARY");
          reply = DO;
          break;
        default:
          if(debug > 2) logger.debug("<UNKNOWN,"+b+">");
          reply = DONT;
          break;
        }
        if(debug > 1) logger.debug("<"+b+", WILL ="+WILL+">");
        if (reply != sentDX[b+128] || WILL != receivedWX[b+128]) {
          sendbuf[0]=IAC;
          sendbuf[1]=reply;
          sendbuf[2]=b;
          write(sendbuf);
          sentDX[b+128] = reply;
          receivedWX[b+128] = WILL;
        }
        neg_state = STATE_DATA;
        break;
      case STATE_IACWONT:
        switch(b) {
        case TELOPT_ECHO:
          if(debug > 2) logger.debug("ECHO");
          setLocalEcho(true);
          reply = DONT;
          break;
        case TELOPT_SGA:
          if(debug > 2) logger.debug("SGA");
          reply = DONT;
          break;
        case TELOPT_EOR:
          if(debug > 2) logger.debug("EOR");
          reply = DONT;
          break;
        case TELOPT_BINARY:
          if(debug > 2) logger.debug("BINARY");
          reply = DONT;
          break;
        default:
          if(debug > 2) logger.debug("<UNKNOWN,"+b+">");
          reply = DONT;
          break;
        }
        if(reply != sentDX[b+128] || WONT != receivedWX[b+128]) {
          sendbuf[0]=IAC;
          sendbuf[1]=reply;
          sendbuf[2]=b;
          write(sendbuf);
          sentDX[b+128] = reply;
          receivedWX[b+128] = WILL;
        }
        neg_state = STATE_DATA;
        break;
      case STATE_IACDO:
        switch (b) {
        case TELOPT_ECHO:
          if(debug > 2) logger.debug("ECHO");
          reply = WILL;
          setLocalEcho(true);
          break;
        case TELOPT_SGA:
          if(debug > 2) logger.debug("SGA");
          reply = WILL;
          break;
        case TELOPT_TTYPE:
          if(debug > 2) logger.debug("TTYPE");
          reply = WILL;
          break;
        case TELOPT_BINARY:
          if(debug > 2) logger.debug("BINARY");
          reply = WILL;
          break;
        case TELOPT_NAWS:
          if(debug > 2) logger.debug("NAWS");
          Dimension size = getWindowSize();
          receivedDX[b] = DO;
          if(size == null) {
            // this shouldn't happen
            write(IAC);
            write(WONT);
            write(TELOPT_NAWS);
            reply = WONT;
            sentWX[b] = WONT;
            break;
          }
          reply = WILL;
          sentWX[b] = WILL;
          sendbuf[0]=IAC;
          sendbuf[1]=WILL;
          sendbuf[2]=TELOPT_NAWS;
          write(sendbuf);
          write(IAC);write(SB);write(TELOPT_NAWS);
          write((byte) (size.width >> 8));
          write((byte) (size.width & 0xff));
          write((byte) (size.height >> 8));
          write((byte) (size.height & 0xff));
          write(IAC);write(SE);
          break;
        default:
          if(debug > 2) logger.debug("<UNKNOWN,"+b+">");
          reply = WONT;
          break;
        }
        if(reply != sentWX[128+b] || DO != receivedDX[128+b]) {
          sendbuf[0]=IAC;
          sendbuf[1]=reply;
          sendbuf[2]=b;
          write(sendbuf);
          sentWX[b+128] = reply;
          receivedDX[b+128] = DO;
        }
        neg_state = STATE_DATA;
        break;
      case STATE_IACDONT:
        switch (b) {
        case TELOPT_ECHO:
          if(debug > 2) logger.debug("ECHO");
          reply = WONT;
          setLocalEcho(false);
          break;
        case TELOPT_SGA:
          if(debug > 2) logger.debug("SGA");
          reply = WONT;
          break;
        case TELOPT_NAWS:
          if(debug > 2) logger.debug("NAWS");
          reply = WONT;
          break;
        case TELOPT_BINARY:
          if(debug > 2) logger.debug("BINARY");
          reply = WONT;
          break;
        default:
          if(debug > 2) logger.debug("<UNKNOWN,"+b+">");
          reply = WONT;
          break;
        }
        if(reply != sentWX[b+128] || DONT != receivedDX[b+128]) {
          write(IAC);write(reply);write(b);
          sentWX[b+128] = reply;
          receivedDX[b+128] = DONT;
        }
        neg_state = STATE_DATA;
        break;
      case STATE_IACSBIAC:
        if(debug > 2) logger.debug(""+b+" ");
        if (b == IAC) {
          sbcount = 0;
          current_sb = b;
          neg_state = STATE_IACSBDATA;
        } else {
          logger.debug("(bad) "+b+" ");
          neg_state = STATE_DATA;
        }
        break;
      case STATE_IACSB:
        if(debug > 2) logger.debug(""+b+" ");
        switch (b) {
        case IAC:
          neg_state = STATE_IACSBIAC;
          break;
        default:
          current_sb = b;
          sbcount = 0;
          neg_state = STATE_IACSBDATA;
          break;
        }
        break;
      case STATE_IACSBDATA:
        if (debug > 2) logger.debug(""+b+" ");
        switch (b) {
        case IAC:
          neg_state = STATE_IACSBDATAIAC;
          break;
        default:
          sbbuf[sbcount++] = b;
          break;
        }
        break;
      case STATE_IACSBDATAIAC:
        if (debug > 2) logger.debug(""+b+" ");
        switch (b) {
        case IAC:
          neg_state = STATE_IACSBDATA;
          sbbuf[sbcount++] = IAC;
          break;
        case SE:
          handle_sb(current_sb,sbbuf,sbcount);
          current_sb = 0;
          neg_state = STATE_DATA;
          break;
        case SB:
          handle_sb(current_sb,sbbuf,sbcount);
          neg_state = STATE_IACSB;
          break;
        default:
          neg_state = STATE_DATA;
          break;
        }
        break;
      default:
        if (debug > 1) 
          logger.debug("This should not happen: "+neg_state+" ");
        neg_state = STATE_DATA;
        break;
      }
    }
    // shrink tempbuf to new processed size.
    byte[] xb = new byte[count-boffset];
    System.arraycopy(tempbuf,boffset,xb,0,count-boffset);
    tempbuf = xb;
    return noffset;
  }

  public void inputfeed(byte[] b, int len) {
    byte[] xb = new byte[tempbuf.length+len];

    System.arraycopy(tempbuf,0,xb,0,tempbuf.length);
    System.arraycopy(b,0,xb,tempbuf.length,len);
    tempbuf = xb;
  }
}
