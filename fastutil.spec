%define section free

Name:           fastutil
Version:        6.2.1
Release:        1
Epoch:          0
Summary:        Fast & compact type-specific Java utility classes
Group:          Development/Libraries/Java
License:        Apache License 2.0
Source0:        http://fastutil.dsi.unimi.it/fastutil-%{version}-src.tar.gz
URL:            http://fastutil.dsi.unimi.it/
BuildArch:      noarch
Requires:       java >= 1.5.0
Provides:	fastutil
Provides:	fastutil5
Obsoletes:	fastutil5
BuildRequires:  ant, make, gcc, /bin/bash
BuildRequires:  java-javadoc
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}-buildroot

%description
fastutil extends the Java Collections Framework by providing type-specific
maps, sets, lists and priority queues with a small memory footprint and
fast access and insertion; provides also big (64-bit) arrays, sets and
lists, and fast, practical I/O classes for binary and text files.

%package        javadoc
Summary:        Javadoc for %{name}
Group:          Development/Documentation

%description    javadoc
Javadoc for %{name}.

%prep
%setup -q -n fastutil-%{version}

%build
make -s sources
export CLASSPATH=
ant \
  -Dj2se.apiurl=%{_javadocdir}/java \
  jar javadoc

%install
rm -rf $RPM_BUILD_ROOT
# jars
install -dm 755 $RPM_BUILD_ROOT%{_javadir}
install -pm 644 %{name}-%{version}.jar $RPM_BUILD_ROOT%{_javadir}
ln -s %{name}-%{version}.jar $RPM_BUILD_ROOT%{_javadir}/%{name}.jar
# javadoc
install -dm 755 $RPM_BUILD_ROOT%{_javadocdir}/%{name}-%{version}
cp -pr docs/* $RPM_BUILD_ROOT%{_javadocdir}/%{name}-%{version}
ln -s %{name}-%{version} $RPM_BUILD_ROOT%{_javadocdir}/%{name} # ghost symlink


%clean
rm -rf $RPM_BUILD_ROOT


%post javadoc
rm -f %{_javadocdir}/%{name}
ln -s %{name}-%{version} %{_javadocdir}/%{name}
 

%files
%defattr(0644,root,root,0755)
%doc README CHANGES LICENSE-2.0
%{_javadir}/*.jar

%files javadoc
%defattr(0644,root,root,0755)
%{_javadocdir}/%{name}-%{version}
%ghost %doc %{_javadocdir}/%{name}
