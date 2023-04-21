# kite-crypto

A file encryption and decryption tool based on the Java programming language. 

- `AES-256` is used to encrypt data
- `CMAC` is used to ensure data integrity

In addition, a toolkit is provided. For example, password generator, UUID generator, Base64 encoding and decoding.

## Usage
1. Make sure that the OS has been configured ≥ JDK11 environment. [Setting up the environment in Java](https://www.geeksforgeeks.org/setting-environment-java/)
2. Download the latest jar file from [Releases](https://github.com/calenwon/kite-crypto/releases).
3. Execute this command in the console:

```shell
java -jar kite-crypto-0.1.0.jar
```

After, the program will automatically create a configuration file named `kite.conf` in the directory where the jar file is located.

4. Edit the configuration file and execute that command again.

> You can also specify a configuration file, like this:
> 
> ```
> java -jar kite-crypto-0.1.0.jar /path/kite.conf
> ```

## Tookit

The following command will generate a 20-character password:

```
$ java -jar kite-crypto-0.1.0.jar -p 20

Numbers: 	91906309043428651124
Mixed: 		LH6hi65ra2RJQRduXreV
Symbols: 	<gH/*P-@]K6zD_0[#/;:

Length: 20
```

Similarly, the following are all arguments.

```
Tookit Arguments:

  -p [length]	Generate password.
		<length> is the length of the password, Default 20.
  
  -u [count]	Generate UUID.
		<count> is the number of generated UUID, Default 20.
  
  -be <text>	Encode <text> in base64.
  
  -bd <text>	Decode <text> in base64.
```