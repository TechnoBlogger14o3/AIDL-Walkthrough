# Android Interface Definition Language (AIDL)

AIDL (Android Interface Definition Language) is similar to other IDLs you might have worked with. It allows you to define the programming interface that both the client and service agree upon in order to communicate with each other using interprocess communication (IPC). On Android, one process cannot normally access the memory of another process. So to talk, they need to decompose their objects into primitives that the operating system can understand, and marshall the objects across that boundary for you. The code to do that marshalling is tedious to write, so Android handles it for you with AIDL.

> **Note**: Using AIDL is necessary only if you allow clients from different applications to access your service for IPC and want to handle multithreading in your service. If you do not need to perform concurrent IPC across different applications, you should create your interface by implementing a Binder or, if you want to perform IPC, but do not need to handle multithreading, implement your interface using a Messenger. Regardless, be sure that you understand Bound Services before implementing an AIDL.

# Steps to define AIDL
 * Create the .aidl file.
 * Implement the interface.
 * Expose the interface to Clients.
 
 # Creating an .aidl file.
 
 ```
interface IAddInterface {
   int add(in int x, in int y);
}
```

# Implement the Interface.
```
IAddInterface.Stub mBinder = new IAddInterface.Stub() {
  @Override
  public int add(int x, int y) throws RemoteException {
  return x + y;
  }
};
```

# Expose the interface to Clients.
```
public class AdditionService extends Service {
    public AdditionService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    IAddInterface.Stub mBinder = new IAddInterface.Stub() {
        @Override
        public int add(int x, int y) throws RemoteException {
            return x + y;
        }
    };
}
```

Now, when a client (such as an activity) calls bindService() to connect to this service, the client's onServiceConnected() callback receives the mBinder instance returned by the service's onBind() method.

The client must also have access to the interface class, so if the client and service are in separate applications, then the client's application must have a copy of the .aidl file in its src/ directory (which generates the android.os.Binder interface—providing the client access to the AIDL methods).

When the client receives the IBinder in the onServiceConnected() callback, it must call **IAddInterface.Stub.asInterface(service) to cast the returned parameter to IAddInterface** type. For example:

```
private IAddInterface iAddInterface;
  ServiceConnection serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      iAddInterface = IAddInterface.Stub.asInterface(service);
    }
    @Override
    public void onServiceDisconnected(ComponentName name) {
    }
};
```
# How it works

AIDL uses the binder kernel driver to make calls. When you make a call, a method identifier and all of the objects are packed onto a buffer and copied to a remote process where a binder thread waits to read the data. Once a binder thread receives data for a transaction, the thread looks up a native stub object in the local process, and this class unpacks the data and makes a call on a local interface object. This local interface object is the one a server process creates and registers. When calls are made in the same process and the same backend, no proxy objects exist, and so calls are direct without any packing or unpacking.

For more clarity go through this [link.](https://source.android.com/devices/architecture/aidl/overview)


