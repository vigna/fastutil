%define name		fastUtil
%define version		2.11
%define release		1jpp
%define javadir		%{_datadir}/java
%define javadocdir	%{_datadir}/javadoc
%define section		free

Name:           %{name}
Version:        %{version}
Release:        %{release}
Summary:        fastUtil: Fast & compact specialized utility classes for Java
License:        LGPL
Source0:	%{name}-%{version}.tar.gz
URL:            http://vigna.dsi.unimi.it/%{name}/
Group:          Development/Java
BuildRoot:      %{_tmppath}/%{name}-%{version}-buildroot
BuildArch:      noarch
Provides:	%{name}
Vendor:		JPackage project

%description
fastUtil provides type-specialized maps and sets with a small memory
footprint and much (2 to 10 times) faster access and insertion.

%package manual
Summary:        Manual for %{name}
Group:          Development/Java

%description manual
Documentation for %{name}.

%package javadoc
Summary:        Javadoc for %{name}
Group:          Development/Java

%description javadoc
Javadoc for %{name}.

%prep
rm -rf $RPM_BUILD_ROOT
%setup -n %{name}-%{version}

%build

%install
#jars
install -d -m 755 $RPM_BUILD_ROOT%{javadir}
install -m 644 %{name}-%{version}.jar $RPM_BUILD_ROOT%{javadir}/
(cd $RPM_BUILD_ROOT%{javadir} && for jar in *-%{version}*; do ln -sf ${jar} `echo $jar| sed  "s|-%{version}||g"`; done)
#javadoc
install -d -m 755 $RPM_BUILD_ROOT%{javadocdir}/%{name}-%{version}
cp -pr docs/* $RPM_BUILD_ROOT%{javadocdir}/%{name}-%{version}
(cd $RPM_BUILD_ROOT%{javadocdir} && for docdir in *-%{version}*; do ln -sf ${docdir} `echo $docdir| sed  "s|-%{version}||g"`; done)

%clean
rm -rf $RPM_BUILD_ROOT

%files
%defattr(-,root,root)
%{javadir}/*

%files manual
%defattr(-,root,root)
%doc README CHANGES COPYING.LIB

%files javadoc
%defattr(-,root,root)
%{javadocdir}/%{name}-%{version}
%{javadocdir}/%{name}

%changelog
* Mon Dec 9 2002 Sebastiano Vigna <vigna@acm.org> 2.11
- JPackage compatible version
