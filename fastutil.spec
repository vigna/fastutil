%define name            fastutil
%define version         2.52
%define release         1jpp
%define javadir         %{_datadir}/java
%define javadocdir      %{_datadir}/javadoc
%define section         free

Name:           %{name}
Version:        %{version}
Release:        %{release}
Summary:        fastUtil: Fast & compact type-specific utility classes
License:        LGPL
Source0:        http://vigna.dsi.unimi.it/fastUtil/fastUtil-2.52.tar.gz
URL:            http://vigna.dsi.unimi.it/fastUtil/
Group:          Development/Libraries/Java
Vendor:         JPackage Project
Distribution:   JPackage
Provides:	fastUtil fastutil
BuildRequires:  ant, make, gcc, jpackage-utils, /bin/bash
BuildArch:      noarch
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}-buildroot

%description
fastUtil provides type-specific maps and sets with a small memory
footprint and much (up to 10 times) faster access and insertion.  The
classes implement their standard counterpart interface (e.g., Map for
maps) and can be plugged into existing code. Moreover, they provide
additional features (such as bidirectional iterators) that are not
available in the standard classes.

%package javadoc
Summary:        Javadoc for %{name}
Group:          Development/Documentation

%description javadoc
Javadoc for %{name}.

# -----------------------------------------------------------------------------

%prep
%setup -q -n fastUtil-%{version}

find . -name "*.jar" -exec rm -f {} ';'
rm -rf docs

# -----------------------------------------------------------------------------

%build

. %{_bindir}/java-functions
set_jvm

export PATH=$JAVA_HOME/bin:$PATH
export APIURL=http://java.sun.com/j2se/1.4.1/docs/api/
#export ANT_ARGS="$ANT_ARGS -Dbuild.compiler=jikes"

./gencsources.sh
make jar docs

# -----------------------------------------------------------------------------

%install
rm -rf $RPM_BUILD_ROOT

# jars
mkdir -p $RPM_BUILD_ROOT%{javadir}
cp -p fastUtil-%{version}.jar $RPM_BUILD_ROOT%{javadir}/%{name}-%{version}.jar
(cd $RPM_BUILD_ROOT%{javadir} && for jar in *-%{version}*; do ln -sf ${jar} `echo $jar| sed "s|-%{version}||g"`; done)

# javadoc
mkdir -p $RPM_BUILD_ROOT%{javadocdir}/%{name}-%{version}
cp -pr docs/* $RPM_BUILD_ROOT%{javadocdir}/%{name}-%{version}

# -----------------------------------------------------------------------------

%clean
rm -rf $RPM_BUILD_ROOT

# -----------------------------------------------------------------------------

%files
%defattr(0644,root,root,0755)
%doc README CHANGES COPYING.LIB
%{javadir}/*

%files javadoc
%defattr(0644,root,root,0755)
%{javadocdir}/%{name}-%{version}

# -----------------------------------------------------------------------------

%changelog
* Wed Mar 12 2003 Sebastiano Vigna <vigna at acm.org> - 2.52-1jpp
- IMPORTANT: The package name has changed. Please remove manually
  the old package.
- Deleted docs from source tar.
- Merged in patches to Makefile.

* Sun Mar  9 2003 Ville Skyttä <ville.skytta at iki.fi> - 2.51-2jpp
- First official JPackage release.
- Fix Group tags.
- No manual subpackage (there's no manual... :)

* Wed Feb 26 2003 Sebastiano Vigna <vigna at acm.org> 2.51
- New flexible trim(int) method..

* Tue Feb 18 2003 Sebastiano Vigna <vigna at acm.org> 2.50
- New linked hash tables and reference-based containers.

* Mon Dec 9 2002 Sebastiano Vigna <vigna at acm.org> 2.11
- JPackage compatible version
